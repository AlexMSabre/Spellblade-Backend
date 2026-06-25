package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("ANCESTRIES")
@Data
public class Ancestry {

    @Id private String id;
    private String name;
    private String source;
    private String trait;
    private String size;
    
    public Ancestry(){}
}