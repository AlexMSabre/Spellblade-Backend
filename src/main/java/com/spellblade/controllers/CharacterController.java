package com.spellblade.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.CharacterDAO;
import com.spellblade.model.CharacterObject;
import com.spellblade.model.Inventory;
import com.spellblade.model.InventoryDAO;
import com.spellblade.model.Item;
import com.spellblade.operations.ItemOperations;
import com.spellblade.repository.CharacterObjectRepository;
import com.spellblade.repository.InventoryRepository;
import com.spellblade.repository.ItemLkpRepository;

//the endpoints for everything related to characters
@Controller
public class CharacterController {

    private final CharacterObjectRepository characters;
    private final ItemLkpRepository items;
    private final InventoryRepository inventory;
    private final ItemOperations itemOperations;

    public CharacterController(CharacterObjectRepository characters, ItemLkpRepository items, InventoryRepository inventory) {
        this.characters = characters;
        this.items = items;
        this.inventory = inventory;

        this.itemOperations = new ItemOperations(this.items, this.inventory);
    }

    //creates/finds characters
    //if Id provided, find the character, else create a new one
    @QueryMapping
    public CharacterDAO saveCharacter(@Argument CharacterDAO characterDAO) {

        System.out.println(characterDAO);

        checkProficiencyChange(characterDAO);

        CharacterObject savedCharacter = characters.save(characterDAO.getCharacter());
        itemOperations.saveUpdateItems(characterDAO.getInventory(), savedCharacter.getId());

        List<InventoryDAO> inventoryDAOs = itemOperations.createInventoryDAOList(savedCharacter.getId());

        return new CharacterDAO(inventoryDAOs, savedCharacter);
    }

    //See Tin
    @QueryMapping
    public List<CharacterObject> charactersByUserId(@Argument String userId) {
        return characters.findByUserId(userId);
    }

    @QueryMapping
    public CharacterDAO characterById(@Argument String characterId) {
        CharacterObject character = characters.findById(characterId).orElseThrow();
        List<InventoryDAO> inventoryDAOs = itemOperations.createInventoryDAOList(character.getId());
        return new CharacterDAO(inventoryDAOs, character);
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
            String[] dummyProf = {" ", "", ""};
            addMissingProficiencies(characterDAO, newProfs, dummyProf);
        }
    }

    public void addMissingProficiencies(CharacterDAO characterDAO, String[] newProfs, String[] oldProfs) {
        for (int i = 0; i < 3; i++) {
            System.out.println("new: " + newProfs[i] + " old: " + oldProfs[i]);
            if (!newProfs[i].equals("null") && !newProfs[i].equals(oldProfs[i])) {

                System.out.println("step 2");
                //if the proficiencies have changed, add a weapon of the new type into the character's inventory
                Item item = items.findById(newProfs[i]).orElseThrow();
                //figure out if the new weapon needs ammo or a qty > 1
                List<Inventory> weaponInventory = itemOperations.calculateDefaultQuantity(item, characterDAO.getCharacter().getId());
                List<InventoryDAO> characterInventory = characterDAO.getInventory();
                
                weaponInventory.forEach((weapon) -> {
                    boolean exists = false;
                    for(InventoryDAO inventoryItem : characterInventory) {
                        if(inventoryItem.getInventory().getItemId().equals(weapon.getItemId())){
                            exists = true;
                            break;
                        }
                    }

                    if(!exists)
                        characterInventory.add(new InventoryDAO(weapon));
                });
                //save the changes
                characterDAO.setInventory(characterInventory);
            }
        }
    }
}
