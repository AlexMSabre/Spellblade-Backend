package com.spellblade.model;

import lombok.Data;

@Data
public class InventoryDAO {

    private Inventory inventory;
    private Item item;

    public InventoryDAO(){}

    public InventoryDAO(Inventory inventory){
        this.inventory = inventory;
    }

    public InventoryDAO(Inventory inventory, Item item){
        this.inventory = inventory;
        this.item = item;
    }

}