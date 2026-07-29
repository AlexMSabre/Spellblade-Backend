package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("SPELL_CHARACTER")
@Data
public class SpellCharacter {

    @Id private String id;
    private String characterId;
    private String spellId;
    
    public SpellCharacter(){}

    public SpellCharacter(String characterId, String spellId){
        this.characterId = characterId;
        this.spellId = spellId;
    }
}