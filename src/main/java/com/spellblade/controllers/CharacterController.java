package com.spellblade.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.CharacterDAO;
import com.spellblade.model.CharacterObject;
import com.spellblade.model.InventoryDAO;
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

    public CharacterController(CharacterObjectRepository characters, ItemLkpRepository items, InventoryRepository inventory){
        this.characters = characters;
        this.items = items;
        this.inventory = inventory;
    }

    //creates/finds characters
    //if Id provided, find the character, else create a new one
    @QueryMapping
    public CharacterDAO saveCharacter(@Argument CharacterDAO characterDAO) {
        CharacterObject savedCharacter = characters.save(characterDAO.getCharacter());

        ItemOperations itemOperations = new ItemOperations(this.items, this.inventory);
        itemOperations.saveUpdateItems(characterDAO.getInventory(), savedCharacter.getId());
        
        List<InventoryDAO> inventoryDAOs = itemOperations.createInventoryDAOList(savedCharacter.getId());
        
        return new CharacterDAO(inventoryDAOs, savedCharacter);
    }

    //See Tin
    @QueryMapping
    public List<CharacterObject> charactersByUserId(@Argument String userId) {
        return characters.findByUserId(userId);
    }
}
