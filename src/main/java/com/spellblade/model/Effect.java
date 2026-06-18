package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("EFFECTS")
@Data
public class Effect {

    @Id private String id;
    private String name;
    private String description;
    private String charProperty;
    private int effectType;
    private String conditionalCheck;
    private String effect;
    
    public Effect(){}
}