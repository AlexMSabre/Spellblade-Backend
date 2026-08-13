package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.spellblade.model.inners.Attack;
import com.spellblade.model.inners.Special;

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
    private String description;
    private String properties;
    private Attack attack;
    private Special special;
    private String effectName;
    private int baseCost;
    private String rarity;

    

    
}