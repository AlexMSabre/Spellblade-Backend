package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("SPELL")
@Data
public class Spell {

    @Id private String id;
    private String name;
    private String spellType;
    private String source;
    private String actionCost;
    private int manaCost;
    private String range;
    private String duration;
    private String description;
    private String effectAmount;
    private String effectConditional;
    private String effectType;
    private String tags;
    
    public Spell(){}
}