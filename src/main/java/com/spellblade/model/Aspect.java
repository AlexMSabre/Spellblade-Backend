package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("ASPECTS")
@Data
public class Aspect {

    @Id private String id;
    private String name;
    private String talentName;
    private int flag;
    private String description;
    
    public Aspect(){}
}