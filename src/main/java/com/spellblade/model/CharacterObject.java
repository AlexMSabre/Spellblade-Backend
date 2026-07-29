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
	private int attributeLevel;
    private String talent1;
    private String talent2;
    private int attribute1;
    private int attribute2;
    private String ancestry;
    private String background;
    private int baseFitness;
    private int basePrecision;
    private int baseFocus;
    private int baseSense;
    private String size;
    private String proficiencies;

    public CharacterObject(){}

}