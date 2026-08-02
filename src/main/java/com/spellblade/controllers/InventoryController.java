package com.spellblade.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.Inventory;
import com.spellblade.model.Item;
import com.spellblade.model.dao.InventoryDAO;
import com.spellblade.model.screens.EquipmentScreen;
import com.spellblade.operations.ItemOperations;
import com.spellblade.repository.InventoryRepository;
import com.spellblade.repository.ItemLkpRepository;
import com.spellblade.repository.PackRepository;

//the endpoints for everything related to characters
@Controller
public class InventoryController {

    @Autowired
    private ItemLkpRepository items;
    @Autowired
    private InventoryRepository inventory;
    @Autowired
    private PackRepository packs;
    private final ItemOperations itemOperations;

    public InventoryController(){
        this.itemOperations = new ItemOperations(items, inventory);
    }

    //creates character item relationships
    @QueryMapping
    public InventoryDAO createInventoryItem(@Argument Inventory inventoryItem) {
        InventoryDAO result = new InventoryDAO(inventory.save(inventoryItem));
        result.setItem(items.findById(inventoryItem.getItemId()).orElseThrow());
        return result;
    }

    @QueryMapping
    public List<InventoryDAO> getCharacterInventory(@Argument String characterId){
        return itemOperations.createInventoryDAOList(characterId);
    }

    @QueryMapping
    public List<InventoryDAO> getCharacterInventoryByType(@Argument String characterId, @Argument String itemType){
        //filters out the list by item type, returning only those that match
        return itemOperations.createInventoryDAOList(characterId)
                .stream()
                .filter(b-> b.getItem().getItemType().contains(itemType))
                .toList();
    }

    //itemtype does not need to be exact 
    @QueryMapping
    public List<Item> getItemListByType(@Argument String itemType) {
        return items.findByItemTypeContainingOrderByItemType(itemType);
    }

    @QueryMapping
    public List<Item> getWeaponList() {
        //gets all weapon and shield items and puts them in one list
        List<Item> results = Stream.concat(
            items.findByItemTypeContainingOrderByItemType("Weapon").stream(),
            items.findByItemTypeContainingOrderByItemType("Shield").stream()
        ).toList();

        //adds spellcasting tools to the list and returns
        return Stream.concat(
            results.stream(),
            items.findByItemTypeContainingOrderByItemType("Spellcasting").stream()
        ).toList();
    }

    @MutationMapping
    public Integer changeItemOwner(@Argument Inventory inventoryItem, @Argument String newOwnerId, @Argument int transferQuantity){
        if(inventoryItem.getQuantity() == transferQuantity){
            inventoryItem.setCharacterId(newOwnerId);
            inventoryItem.setEquipped(false);
            inventory.save(inventoryItem);
        } else {
            inventory.save(new Inventory(inventoryItem.getItemId(), newOwnerId, transferQuantity));
            inventoryItem.setQuantity(inventoryItem.getQuantity()-transferQuantity);
        }
        return 1;
    }
    

    @QueryMapping
    public EquipmentScreen getEquipmentScreen(@Argument String characterId){
        EquipmentScreen result = new EquipmentScreen();
        result.setPacks(packs.findAll());
        result.setItems(items.findAll());
        result.setInventory(characterId.equals("") ? new ArrayList<>() :  inventory.findByCharacterId(characterId));

        return result;
    }
}
