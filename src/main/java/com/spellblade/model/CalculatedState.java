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
    private int evasion;
    private int hexResist;
    private int movement;
    private int encumbrance;
    private int fitness;
    private int technique;
    private int focus;
    private int sense;

    public CalculatedState(){
        
    }

    public CalculatedState(CharacterObject character, CharacterState state){
        setFitness(character.getBaseFitness());
        setTechnique(character.getBaseTechnique());
        setFocus(character.getBaseFocus());
        setSense(character.getBaseSense());
        setHitPoints(state.getHitPoints());
        setManaPoints(state.getManaPoints());
        setArmor(state.getArmor());
        setMovement(6);
    }

}