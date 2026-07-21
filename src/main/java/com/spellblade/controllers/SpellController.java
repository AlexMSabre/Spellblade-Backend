package com.spellblade.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.Spell;
import com.spellblade.repository.SpellCharacterRepository;
import com.spellblade.repository.SpellRepository;

//the endpoints for everything related to character states and effects
@Controller
public class SpellController {
    
    @Autowired
    private SpellCharacterRepository spellCharacters;
    @Autowired
    private SpellRepository spells;

    @QueryMapping
    public List<Spell> getAllSpells() {
        return spells.findAll();
    }

    
}
