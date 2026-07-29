package com.spellblade.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.CharacterObject;
import com.spellblade.model.CharacterState;
import com.spellblade.model.Inventory;
import com.spellblade.model.Item;
import com.spellblade.model.Talent;
import com.spellblade.model.dao.BackgroundDAO;
import com.spellblade.model.dao.CharacterDAO;
import com.spellblade.model.dao.InventoryDAO;
import com.spellblade.operations.AttributeOperations;
import com.spellblade.operations.ItemOperations;
import com.spellblade.operations.StateOperations;
import com.spellblade.operations.TraitOperations;
import com.spellblade.repository.AncestryRepository;
import com.spellblade.repository.AttributeLkpRepository;
import com.spellblade.repository.BackgroundRepository;
import com.spellblade.repository.CharacterObjectRepository;
import com.spellblade.repository.CharacterStateRepository;
import com.spellblade.repository.EffectRepository;
import com.spellblade.repository.InventoryRepository;
import com.spellblade.repository.ItemLkpRepository;
import com.spellblade.repository.SpellCharacterRepository;
import com.spellblade.repository.TalentLkpRepository;
import com.spellblade.repository.TraitRepository;

//the endpoints for everything related to characters
@Controller
public class CharacterController {

    private final CharacterObjectRepository characters;
    private final ItemLkpRepository items;
    private final TalentLkpRepository talents;
    @Autowired
    private CharacterStateRepository states;

    private final TraitOperations traitOperations;

    private final ItemOperations itemOperations;
    private final AttributeOperations attributeOperations;
    private final StateOperations stateOperations;

    @Autowired
    private SpellCharacterRepository spellCharacters;

    public CharacterController(InventoryRepository inventory, AttributeLkpRepository attribute, EffectRepository effects, ItemLkpRepository items,
                             TalentLkpRepository talents, CharacterObjectRepository characters, AncestryRepository ancestries, BackgroundRepository backgrounds, TraitRepository traits){

        this.items = items;
        this.talents = talents;
        this.characters = characters;
        this.itemOperations = new ItemOperations(items, inventory);
        this.attributeOperations = new AttributeOperations(attribute);
        this.stateOperations = new StateOperations(effects, talents, characters);
        this.traitOperations = new TraitOperations(ancestries, traits, backgrounds);
    }

    //creates/finds characters
    //if Id provided, find the character, else create a new one
    @QueryMapping
    public CharacterDAO saveCharacter(@Argument CharacterDAO characterDAO) {
        
        System.out.println(characterDAO);

        checkProficiencyChange(characterDAO);

        characterDAO.getCharacterState().setCharacterId(characterDAO.getCharacter().getId());

        CharacterState characterState = states.save(characterDAO.getCharacterState());

        CharacterObject savedCharacter = characters.save(characterDAO.getCharacter());
        itemOperations.saveUpdateItems(characterDAO.getInventory(), savedCharacter.getId());

        List<InventoryDAO> inventoryDAOs = itemOperations.createInventoryDAOList(savedCharacter.getId());

        if(characterDAO.getSpells() != null) {
            spellCharacters.saveAll(characterDAO.getSpells());
        }

        return new CharacterDAO(inventoryDAOs, savedCharacter, characterState);
    }

    //See Tin
    @QueryMapping
    public List<CharacterObject> charactersByUserId(@Argument String userId) {
        return characters.findByUserId(userId);
    }

//only gets the raw character data and full inventory data
    @QueryMapping
    public CharacterDAO characterById(@Argument String characterId) {
        CharacterObject character = characters.findById(characterId).orElseThrow();
        List<InventoryDAO> inventoryDAOs = itemOperations.createInventoryDAOList(character.getId());
        CharacterState characterState = states.findByCharacterId(characterId).orElse(new CharacterState());
        return new CharacterDAO(inventoryDAOs, character, characterState);
    }

    //gets all of the data needed to read the character sheet.
    @QueryMapping
    public CharacterDAO fullCharacterById(@Argument String characterId) {

        CharacterDAO result = new CharacterDAO();
        CharacterObject character = characters.findById(characterId).orElseThrow();
        result.setCharacter(character);
        result.setInventory(itemOperations.createInventoryDAOList(characterId));
        result.setProficiencies(itemOperations.getProficiencyNames(character.getProficiencies()));
        
        result.setAttributes(attributeOperations.getAttributeFromTalentAndFlags(character));
        List<Talent> charTalents = new ArrayList<>();
        charTalents.add(talents.findByName(character.getTalent1()).orElseThrow());
        charTalents.add(talents.findByName(character.getTalent2()).orElseThrow());
        result.setTalents(charTalents);

        result.setCharacterState(states.findByCharacterId(characterId).orElse(new CharacterState()));
        result.setCalculatedState(stateOperations.calculateState(result.getCharacterState(), character));

        result.setSpells(spellCharacters.findByCharacterId(characterId));

        result.setTraitData(traitOperations.collectTraitDetails(character));

        return result;
    }

    public void checkProficiencyChange(CharacterDAO characterDAO) {
        //get the proficincy values for analysis
        CharacterObject newCharacter = characterDAO.getCharacter();
        //find the old character values.  if this is a new character (id ==null) or there are no pre-exising values, return a blank object
        CharacterObject oldCharacter = newCharacter.getId() != null ? characters.findById(newCharacter.getId()).orElse(new CharacterObject()) : new CharacterObject();
        String[] newProfs = newCharacter.getProficiencies().split(",");
        if (oldCharacter.getProficiencies() != null) {
            System.out.println("step 1");
            //process and compare the inventory values
            String[] oldProfs = oldCharacter.getProficiencies().split(",");
            addMissingProficiencies(characterDAO, newProfs, oldProfs);

        } else {
            //else add all to the inventory
            String[] dummyProf = {};
            addMissingProficiencies(characterDAO, newProfs, dummyProf);
        }
    }

    public void addMissingProficiencies(CharacterDAO characterDAO, String[] newProfs, String[] oldProfs) {
        for (int i = 0; i < newProfs.length; i++) {
            //if the new prof is not null and if there are more new proficiencies than old proficiencies, or the new proficiency is different from the old proficiency
                if (!newProfs[i].equals("null") && (i>=oldProfs.length || !newProfs[i].equals(oldProfs[i]))) {

                    System.out.println("step 2");
                    //if the proficiencies have changed, add a weapon of the new type into the character's inventory
                    Item item = items.findById(newProfs[i]).orElseThrow();
                    //figure out if the new weapon needs ammo or a qty > 1
                    List<Inventory> weaponInventory = itemOperations.calculateDefaultQuantity(item, characterDAO.getCharacter().getId());
                    List<InventoryDAO> characterInventory = characterDAO.getInventory();
                    
                    //check to confirm that the weapon does not already exist in the inventory
                    weaponInventory.forEach((weapon) -> {
                        boolean exists = false;
                        for(InventoryDAO inventoryItem : characterInventory) {
                            if(inventoryItem.getInventory().getItemId().equals(weapon.getItemId())){
                                exists = true;
                                break;
                            }
                        }
                        //if it isn't in the inventory already, add it
                        if(!exists)
                            characterInventory.add(new InventoryDAO(weapon));
                    });
                    //save the changes
                    characterDAO.setInventory(characterInventory);
                }
        }
    }
    

    @QueryMapping
    public BackgroundDAO getBackgroundScreen(@Argument String source){
        return traitOperations.collectBackgroundScreenData(source);
    }
}
