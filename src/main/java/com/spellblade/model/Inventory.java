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
    private int quantity;
    
    public Inventory(){}

    public Inventory(String itemId, String characterId, int quantity){
        this.itemId = itemId;
        this.characterId = characterId;
        this.quantity = quantity;
    }
}