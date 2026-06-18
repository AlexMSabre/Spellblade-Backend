package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("TALENTS")
@Data
public class Talent {

    @Id private String id;
    private String name;
    private String abilities;
    private String description;
    private int hpBonus;
    private String prioritySkills;
    private String role;
    private int complexity;
    private String keystone;
    private String capstone;
    private String effectId;
    private String keystoneEffectId;
    
    public Talent(){}

//bare minimum constructor
    public Talent(String role){
        this.role = role;
    }
}