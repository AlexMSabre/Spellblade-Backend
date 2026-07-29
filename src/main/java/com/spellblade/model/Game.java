package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("GAME")
@Data
public class Game {

    @Id private String id;
    private String ownerId;
    private String characterIds;
    private String playerIds;
    private String joinCode;

    public Game (){}

    public Game(String ownerId){
        this.ownerId = ownerId;
    }
}