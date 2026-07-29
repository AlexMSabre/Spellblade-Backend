package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("PARTY_INVENTORY")
@Data
public class PartyInventory {

    @Id private String id;
    private String gameId;
    private String itemId;
    private String location;
}