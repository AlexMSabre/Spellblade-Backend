package com.spellblade.model;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Data;

@Document("PACK")
@Data
public class Pack{
    
    @Id String id;
    String name;
    String background;
    int currency;
    int rations;
    int salves;
    String reagents;
    String materials;
    String innerwear;
    String outerwear;
    String items;
}