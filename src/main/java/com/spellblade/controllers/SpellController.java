package com.spellblade.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.Spell;
import com.spellblade.model.SpellCharacter;
import com.spellblade.operations.SpellOperations;
import com.spellblade.repository.SpellCharacterRepository;
import com.spellblade.repository.SpellRepository;

//the endpoints for everything related to character states and effects
@Controller
public class SpellController {
    
    private final SpellCharacterRepository spellCharacters;
    private final SpellRepository spells; 
    private final SpellOperations spellOps;

    public SpellController(SpellCharacterRepository spellCharacters, SpellRepository spells){
        this.spells = spells;
        this.spellCharacters = spellCharacters;
        this.spellOps = new SpellOperations(spells, spellCharacters);
    }

    @QueryMapping
    public List<Spell> getFilteredSpells(@Argument List<String> sources){
        List<Spell> result= new ArrayList<>();
        if(!sources.isEmpty())
            sources.forEach(f->result.addAll(spells.findBySourceContaining(f)));
        else
            result.addAll(spells.findAll());
        return result;
    }

    @MutationMapping
    public int saveSpellCharacter(@Argument List<SpellCharacter> characterSpells){
        spellOps.saveUpdateSpells(characterSpells);
        return 1;
    }


    
}
