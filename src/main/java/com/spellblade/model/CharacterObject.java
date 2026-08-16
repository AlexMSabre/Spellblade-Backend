package com.spellblade.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.spellblade.model.inners.AncestryInner;
import com.spellblade.model.inners.BackgroundInner;
import com.spellblade.model.inners.CharacterState;

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
    private Talent talent1;
    private Talent talent2;
    private List<Attribute> attributes1;
    private List<Attribute> attributes2;
    private AncestryInner ancestry;
    private BackgroundInner background;
    private int baseFitness;
    private int basePrecision;
    private int baseFocus;
    private int baseSense;
    private String size;
    private List<String> proficiencies;
    private CharacterState state;
    private String patronDamageType;
    private String elementDamageType;
    private String elementName;

    public CharacterObject(){}

}