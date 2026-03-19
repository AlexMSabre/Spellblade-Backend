package com.spellblade.operations;

import java.util.ArrayList;
import java.util.List;

import com.spellblade.model.Inventory;
import com.spellblade.model.InventoryDAO;
import com.spellblade.model.Item;
import com.spellblade.repository.InventoryRepository;
import com.spellblade.repository.ItemLkpRepository;


public class ItemOperations{
    
    private final ItemLkpRepository items;
    private final InventoryRepository inventory;

    public ItemOperations(ItemLkpRepository items, InventoryRepository inventory){
        this.items = items;
        this.inventory = inventory;
    }

    public void saveUpdateItems(List<InventoryDAO> newInventoryDAO, String characterId){
        List<Inventory> newInventory = extractInventoryList(newInventoryDAO);
        List<Inventory> removeList = getcharacterInventory(characterId);

        for(Inventory inventoryItem : newInventory){
            removeList.removeIf((e)->e.getId().equals(inventoryItem.getId()));
            inventoryItem.setCharacterId(characterId);
            inventory.save(inventoryItem);
        }

        removeList.forEach((e)->{inventory.delete(e);});
    }

    public List<Inventory> extractInventoryList(List<InventoryDAO> newInventoryDAO){
        List<Inventory> newInventory = new ArrayList<>();
        for(InventoryDAO inv: newInventoryDAO){
            newInventory.add(inv.getInventory());
        }
        return newInventory;
    }

    public List<Inventory> getcharacterInventory(String characterId){
        return inventory.findByCharacterId(characterId);
    }

    public List<InventoryDAO> createInventoryDAOList(String characterId){
        List<InventoryDAO> results = new ArrayList<>();
        //finds the inventory item, then gets the item data, then puts both into an output object.
        inventory.findByCharacterId(characterId).forEach(a -> {
            Item itemData = items.findById(a.getItemId()).orElseThrow();
            InventoryDAO inventoryItem = new InventoryDAO(a, itemData);
            results.add(inventoryItem);
        });
        return results;
    }
}