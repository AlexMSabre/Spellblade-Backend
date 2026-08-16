package com.spellblade.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.CharacterObject;
import com.spellblade.model.Effect;
import com.spellblade.model.dao.CharacterDAO;
import com.spellblade.model.dao.InventoryDAO;
import com.spellblade.model.dao.SpellDAO;
import com.spellblade.model.screens.BackgroundScreen;
import com.spellblade.operations.ItemOperations;
import com.spellblade.operations.SpellOperations;
import com.spellblade.operations.StateOperations;
import com.spellblade.operations.TraitOperations;
import com.spellblade.repository.AncestryRepository;
import com.spellblade.repository.AttributeLkpRepository;
import com.spellblade.repository.BackgroundRepository;
import com.spellblade.repository.CharacterObjectRepository;
import com.spellblade.repository.EffectRepository;
import com.spellblade.repository.InventoryRepository;
import com.spellblade.repository.ItemLkpRepository;
import com.spellblade.repository.SpellCharacterRepository;
import com.spellblade.repository.SpellRepository;
import com.spellblade.repository.TalentLkpRepository;
import com.spellblade.repository.TraitRepository;

//the endpoints for everything related to characters
@Controller
public class CharacterController {

    private final CharacterObjectRepository characters;
    private final ItemLkpRepository items;

    private final TraitOperations traitOperations;

    private final ItemOperations itemOperations;
    private final StateOperations stateOperations;
    private final SpellOperations spellOperations;
    private final EffectRepository effects;

    public CharacterController(InventoryRepository inventory, AttributeLkpRepository attribute, EffectRepository effects, ItemLkpRepository items,
                            TalentLkpRepository talents, CharacterObjectRepository characters, AncestryRepository ancestries, BackgroundRepository backgrounds,
                            TraitRepository traits, SpellRepository spells, SpellCharacterRepository spellCharacters){

        this.items = items;
        this.characters = characters;
        this.effects = effects;
        this.itemOperations = new ItemOperations(items, inventory);
        this.stateOperations = new StateOperations(effects);                                                                
        this.traitOperations = new TraitOperations(ancestries, traits, backgrounds, effects);
        this.spellOperations = new SpellOperations(spells, spellCharacters);
    }

    //creates/finds characters
    //if Id provided, find the character, else create a new one
    @QueryMapping
    public CharacterDAO saveCharacter(@Argument CharacterDAO characterDAO) {

        System.out.println(characterDAO.getCharacter().getProficiencies().toString());
        CharacterObject savedCharacter = characters.save(characterDAO.getCharacter());
        System.out.println(savedCharacter.getProficiencies());
        itemOperations.saveUpdateItems(characterDAO.getInventory(), savedCharacter.getId());

        List<InventoryDAO> inventoryDAOs = itemOperations.createInventoryDAOList(savedCharacter.getId());

        if(!characterDAO.getSpells().isEmpty())
            characterDAO.getSpells().get(0).getSpellCharacter().setCharacterId(savedCharacter.getId());

        List<SpellDAO> characterSpells = spellOperations.saveUpdateSpellDAOs(characterDAO.getSpells());

        return new CharacterDAO(inventoryDAOs, savedCharacter, characterSpells);
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
        List<SpellDAO> characterSpells = spellOperations.createSpellDAOList(characterId);
        return new CharacterDAO(inventoryDAOs, character, characterSpells);
    }

    //gets all of the data needed to read the character sheet.
    @QueryMapping
    public CharacterDAO fullCharacterById(@Argument String characterId) {

        CharacterDAO result = new CharacterDAO();
        CharacterObject character = characters.findById(characterId).orElseThrow();
        result.setCharacter(character);
        result.setInventory(itemOperations.createInventoryDAOList(characterId));

        result.setCalculatedState(stateOperations.calculateState(character));

        result.setSpells(spellOperations.createSpellDAOList(characterId));

        return result;
    }
    

    @QueryMapping
    public BackgroundScreen getBackgroundScreen(@Argument String source){
        return traitOperations.collectBackgroundScreenData(source);
    }

    @MutationMapping
    public boolean deleteCharacter(@Argument String characterId){
        characters.deleteById(characterId);
        return true;
    }

    @QueryMapping
    public List<Effect> getEffectList(){
        return effects.findAll();
    }
}
