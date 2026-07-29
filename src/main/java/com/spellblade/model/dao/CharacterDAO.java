package com.spellblade.model.dao;

import java.util.List;

import com.spellblade.model.CalculatedState;
import com.spellblade.model.CharacterObject;
import com.spellblade.model.SpellCharacter;

import lombok.Data;

@Data
public class CharacterDAO {

    private List<InventoryDAO> inventory;
    private CharacterObject character;
    private List<ProficiencyDAO> proficiencies;
    private CalculatedState calculatedState;
    private List<SpellCharacter> spells;

    public CharacterDAO() {}

    public CharacterDAO(CharacterObject character){
        this.character = character;
    }

    public CharacterDAO(List<InventoryDAO> inventory, CharacterObject character,List<SpellCharacter> spells){
        this.inventory = inventory;
        this.character = character;
        this.spells = spells;
    }

    public CharacterDAO(List<InventoryDAO> inventory, CharacterObject character,
        List<ProficiencyDAO> proficiencies, CalculatedState calculatedState, List<SpellCharacter> spells
        ){
        this.inventory = inventory;
        this.character = character;
        this.proficiencies = proficiencies;
        this.calculatedState = calculatedState; 
        this.spells = spells;
    }

}