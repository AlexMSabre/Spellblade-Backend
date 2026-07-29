package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("TRAIT")
@Data
public class Trait {

    @Id private String id;
    private String name;
    private String traitType;
    private String description;
    private String tags;
    
    public Trait(){}
}