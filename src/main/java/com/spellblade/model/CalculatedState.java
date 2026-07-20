package com.spellblade.model;

import lombok.Data;

//the model for accessing a character's calculated state in the DB
//@ Document Defines this POJO as belonging to the Character repository
//@ Data causes the POJO methods to auto-generate
@Data
public class CalculatedState {

    private String characterId;
	private int hitPointsMax;
    private int hitPoints;
    private int armorMax;
    private int armorMin;
    private int armor;
    private int woundsMax;
    private int wounds;
    private int manaMax;
    private int manaPoints;
    private int spellCapacity;
    private int dexterity;
    private int celerity;
    private int subtlety;
    private int awareness;
    private int evasion;
    private int tenacity;
    private int movement;
    private int encumbrance;
    private int fitness;
    private int precision;
    private int focus;
    private int sense;

    public CalculatedState(){
        
    }

    public CalculatedState(CharacterObject character, CharacterState state, int MOVEBASE){
        setFitness(character.getBaseFitness());
        setPrecision(character.getBasePrecision());
        setFocus(character.getBaseFocus());
        setSense(character.getBaseSense());
        setHitPoints(state.getHitPoints());
        setManaPoints(state.getManaPoints());
        setArmor(state.getArmor());
        setMovement(MOVEBASE);
    }

}