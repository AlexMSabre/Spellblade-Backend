package com.spellblade.model;

import java.util.List;

import lombok.Data;

@Data
public class CharacterDAO {

    private List<InventoryDAO> inventory;
    private CharacterObject character;

    public CharacterDAO() {}

    public CharacterDAO(CharacterObject character){
        this.character = character;
    }

    public CharacterDAO(List<InventoryDAO> inventory, CharacterObject character){
        this.inventory = inventory;
        this.character = character;
    }

}