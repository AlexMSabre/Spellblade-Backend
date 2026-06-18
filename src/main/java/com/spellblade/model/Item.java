package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("ITEM_LKP")
@Data
public class Item {

    @Id private String id;
    private String name;
    private String itemType;
    private String subtype;
    private boolean equippable;
    private String size;
    private double weight;
    private String description1;
    private String description2;
    private String description3;
    private String attack1String;
    private String attack2String;
    private String attack3String; 
    private String effectName;
    private int baseCost;
    private String rarity;
}