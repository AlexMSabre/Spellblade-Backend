package com.spellblade.model.dao;

import com.spellblade.model.Spell;
import com.spellblade.model.SpellCharacter;

import lombok.Data;

@Data
public class SpellDAO {

    private SpellCharacter spellCharacter;
    private Spell spell;

    public SpellDAO(){}

    public SpellDAO(SpellCharacter spellCharacter){
        this.spellCharacter = spellCharacter;
    }

    public SpellDAO(SpellCharacter spellCharacter, Spell spell){
        this.spellCharacter = spellCharacter;
        this.spell = spell;
    }

}