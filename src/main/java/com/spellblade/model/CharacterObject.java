package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

//the model for accessing Characters in the DB
//it is called CharacterObject because Character is already a java type and it helps to be specific
//@ Document Defines this POJO as belonging to the Character repository
//@ Data causes the POJO methods to auto-generate
@Document("CHARACTER")
@Data
public class CharacterObject {

	@Id private String id;
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
    private String proficiencies;
    private int gold;
    private int silver;
    private int copper;

    public CharacterObject(){}

}