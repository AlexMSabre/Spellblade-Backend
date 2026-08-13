package com.spellblade.operations;

import java.util.ArrayList;
import java.util.List;

import com.spellblade.model.Inventory;
import com.spellblade.model.Item;
import com.spellblade.model.dao.InventoryDAO;
import com.spellblade.model.dao.ProficiencyDAO;
import com.spellblade.repository.InventoryRepository;
import com.spellblade.repository.ItemLkpRepository;


public class ItemOperations{
    
    private final ItemLkpRepository items;
    private final InventoryRepository inventory;

    public ItemOperations(ItemLkpRepository items, InventoryRepository inventory ){
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

    public List<InventoryDAO>  createInventoryDAOList(String characterId){
        List<InventoryDAO> results = new ArrayList<>();
        //finds the inventory item, then gets the item data, then puts both into an output object.
        inventory.findByCharacterId(characterId).forEach(a -> {
            Item itemData = items.findById(a.getItemId()).orElseThrow();
            InventoryDAO inventoryItem = new InventoryDAO(a, itemData);
            results.add(inventoryItem);
        });
        return results;
    }

    public List<Inventory> calculateDefaultQuantity(Item item, String characterId){
        int quantity = 1;
        boolean hasAmmo = false;
        //checks if the item requires ammo.  
        //all weapon types should have the name of the ammo type as the second word
        String[] itemTypeName = item.getItemType().split(" ");
        if(itemTypeName.length > 1){
            switch(item.getItemType().split(" ")[1]){
                case "Bullet" ->  {quantity = 24; hasAmmo = true;} 
                case "Arrow" -> {quantity = 30; hasAmmo = true;}
            }
        }

        //checks if the subtype is one that requires a qty greater than 1
        switch(item.getSubtype()){
            case "Boomerang", "Nail", "Stake" -> quantity = 3;
            case "Throwing Knife" -> quantity = 8;
            case "Shuriken" -> quantity = 12;
            case "Javelin" -> quantity = 4;
        }

        //if it doesnt have ammo, make just the one inventory entry
        //if it does, make an entry for the weapon and another for the ammo
        List<Inventory> result = new ArrayList<>();
        if(!hasAmmo)
            result.add(new Inventory(item.getId(), characterId, quantity));
        else {
            Item ammo = items.findByName(item.getItemType().split(" ")[1]);
            result.add(new Inventory(item.getId(), characterId, 1));
            result.add(new Inventory(ammo.getId(), characterId, quantity));
        }
        return result;
    }

    public List<ProficiencyDAO> getProficiencyNames(List<String> proficiencies){
        //split the proficiencies apart
        List<ProficiencyDAO> results = new ArrayList();

        for(String prof : proficiencies){
            //if a character has mastery in a weapon, it will be marked with a "-m" at the end.
            //so remove the -m if it exists before grabbing the data, 
            if(!prof.equals("null")){
                String[] mastery = prof.split("-");
                Item result = items.findByName(mastery[0]);
                //and use the length to tell if -m exists
                results.add(new ProficiencyDAO(result, mastery.length>1));
            }
        }
        return results;
    }
}