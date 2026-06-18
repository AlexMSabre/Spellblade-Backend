package com.spellblade.model.dao;

import java.util.List;

import com.spellblade.model.Aspect;
import com.spellblade.model.CalculatedState;
import com.spellblade.model.CharacterObject;
import com.spellblade.model.CharacterState;
import com.spellblade.model.Talent;

import lombok.Data;

@Data
public class CharacterDAO {

    private List<InventoryDAO> inventory;
    private CharacterObject character;
    private List<Aspect> aspects;
    private List<Talent> talents;
    private List<ProficiencyDAO> proficiencies;
    private CharacterState characterState;
    private CalculatedState calculatedState;

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
         List<Aspect> aspects, List<Talent> talents, List<ProficiencyDAO> proficiencies,
         CharacterState characterState, CalculatedState calculatedState){
        this.inventory = inventory;
        this.character = character;
        this.aspects = aspects;
        this.talents = talents;
        this.proficiencies = proficiencies;
        this.characterState = characterState;
        this.calculatedState = calculatedState; 
    }

}