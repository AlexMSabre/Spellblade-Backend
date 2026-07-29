package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("ANCESTRY")
@Data
public class Ancestry {

    @Id private String id;
    private String name;
    private String parent;
    private String source;
    private String trait1;
    private String trait2;
    private String size;
    private String description;
    
    public Ancestry(){}
}