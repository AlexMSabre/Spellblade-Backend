package com.spellblade.model;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
public class Character {

	private ObjectId id;
    private String accountId;
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

    public Character(){}

    public Character(String name, String accountId, String ancestryName, 
                        int ancestryTrait, int aspects1, int aspects2, 
                        int specialty1, int specialty2, int baseFitness, 
                        int baseTechnique, int baseFocus, int baseSense,
                        int gold, int silver, int copper){
        this.name = name;
        this.accountId = accountId;
        this.ancestryName = ancestryName;
        this.ancestryTrait = ancestryTrait;
        this.aspects1 = aspects1;
        this.aspects2 = aspects2;
        this.specialty1 = specialty1;
        this.specialty2 = specialty2;
        this.baseFitness = baseFitness;
        this.baseTechnique = baseTechnique;
        this.baseFocus = baseFocus;
        this.baseSense = baseSense;
        this.gold = gold;
        this.silver = silver;
        this.copper = copper;
    }

}