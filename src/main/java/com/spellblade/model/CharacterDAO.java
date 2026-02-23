package com.spellblade.model;

import lombok.Data;

@Data
public class CharacterDAO {

	private String id;
    private String userId;
	private String name;
	private int aspectLevel;
    private int specialty1;
    private int specialty2;
    private int aspects1;
    private int aspects2;
    private String ancestryName;
    private int ancestryTrait;
    private int baseFitness;
    private int baseTechnique;
    private int baseFocus;
    private int baseSense;
    private int gold;
    private int silver;
    private int copper;

    public CharacterDAO(){}

    public CharacterDAO(Character character){
        this.name = character.getName();
        this.id = character.getId().toString();
        this.userId = character.getUserId().toString();
        this.aspectLevel = character.getAspectLevel();
        this.ancestryName = character.getAncestryName();
        this.ancestryTrait = character.getAncestryTrait();
        this.aspects1 = character.getAspects1();
        this.aspects2 = character.getAspects2();
        this.specialty1 = character.getSpecialty1();
        this.specialty2 = character.getSpecialty2();
        this.baseFitness = character.getBaseFitness();
        this.baseTechnique = character.getBaseTechnique();
        this.baseFocus = character.getBaseFocus();
        this.baseSense = character.getBaseSense();
        this.gold = character.getGold();
        this.silver = character.getSilver();
        this.copper = character.getCopper();
    }

}