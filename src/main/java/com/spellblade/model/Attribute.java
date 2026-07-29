package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("ATTRIBUTE_LKP")
@Data
public class Attribute {

    @Id private String id;
    private String name;
    private String talentName;
    private int flag;
    private String description1;
    private String description2;
    
    public Attribute(){}
}