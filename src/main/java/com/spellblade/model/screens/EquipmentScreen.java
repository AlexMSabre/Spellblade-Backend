package com.spellblade.model.screens;

import java.util.List;

import com.spellblade.model.Inventory;
import com.spellblade.model.Item;
import com.spellblade.model.Pack;

import lombok.Data;

@Data
public class EquipmentScreen {

    private List<Inventory> inventory;
    private List<Item> items;
    private List<Pack> packs;

    public EquipmentScreen(){}

}