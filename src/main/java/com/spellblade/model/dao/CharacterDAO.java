package com.spellblade.model.dao;

import java.util.List;

import com.spellblade.model.Attribute;
import com.spellblade.model.CalculatedState;
import com.spellblade.model.CharacterObject;
import com.spellblade.model.CharacterState;
import com.spellblade.model.SpellCharacter;
import com.spellblade.model.Talent;

import lombok.Data;

@Data
public class CharacterDAO {

    private List<InventoryDAO> inventory;
    private CharacterObject character;
    private List<Attribute> attributes;
    private List<Talent> talents;
    private List<ProficiencyDAO> proficiencies;
    private CharacterState characterState;
    private CalculatedState calculatedState;
    private List<SpellCharacter> spells;
    private TraitsDAO traitData;

    public CharacterDAO() {}

    public CharacterDAO(CharacterObject character){
        this.character = character;
    }

    public CharacterDAO(List<InventoryDAO> inventory, CharacterObject character, CharacterState characterState){
        this.inventory = inventory;
        this.character = character;
        this.characterState = characterState;
    }

    public CharacterDAO(List<InventoryDAO> inventory, CharacterObject character,
         List<Attribute> attributes, List<Talent> talents, List<ProficiencyDAO> proficiencies,
         CharacterState characterState, CalculatedState calculatedState, List<SpellCharacter> spells,
         TraitsDAO traitData){
        this.inventory = inventory;
        this.character = character;
        this.attributes = attributes;
        this.talents = talents;
        this.proficiencies = proficiencies;
        this.characterState = characterState;
        this.calculatedState = calculatedState; 
        this.spells = spells;
        this.traitData = traitData;
    }

}