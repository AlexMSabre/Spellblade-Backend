package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("INVENTORY")
@Data
public class Inventory {

    @Id private String id;
    private String itemId;
    private String characterId;
    private boolean equipped;
    private int proficiency;
    private int quantity;
    
    public Inventory(){}

    public Inventory(String itemId, String characterId, int proficiency, int quantity){
        this.itemId = itemId;
        this.characterId = characterId;
        this.proficiency = proficiency;
        this.quantity = quantity;
    }
}