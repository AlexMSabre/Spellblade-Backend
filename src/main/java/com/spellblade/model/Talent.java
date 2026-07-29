package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("TALENT")
@Data
public class Talent {

    @Id private String id;
    private String name;
    private String ability1;
    private String ability2;
    private String description;
    private int hpBonus;
    private String prioritySkills;
    private String role;
    private int complexity;
    private boolean caster;
    private String keystone;
    private String capstone;
    
    public Talent(){}

//bare minimum constructor
    public Talent(String role){
        this.role = role;
    }
}