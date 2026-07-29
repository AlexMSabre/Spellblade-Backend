package com.spellblade.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.Ancestry;
import com.spellblade.model.Background;
import com.spellblade.model.CalculatedState;
import com.spellblade.model.CharacterObject;
import com.spellblade.model.CharacterState;
import com.spellblade.model.Trait;
import com.spellblade.model.dao.CharacterDAO;
import com.spellblade.operations.StateOperations;
import com.spellblade.repository.AncestryRepository;
import com.spellblade.repository.BackgroundRepository;
import com.spellblade.repository.CharacterObjectRepository;
import com.spellblade.repository.CharacterStateRepository;
import com.spellblade.repository.EffectRepository;
import com.spellblade.repository.TalentLkpRepository;
import com.spellblade.repository.TraitRepository;

//the endpoints for everything related to character states and effects
@Controller
public class StateController {

    @Autowired
    private CharacterStateRepository states;
    @Autowired
    private TraitRepository traits;
    @Autowired
    private AncestryRepository ancestries;
    @Autowired
    private BackgroundRepository backgrounds;
    private final StateOperations stateOps;

    public StateController(EffectRepository effects, TalentLkpRepository talents, CharacterObjectRepository characters){
        stateOps = new StateOperations(effects, talents, characters);
    }

    @QueryMapping
    public CalculatedState calculateState(@Argument CharacterState state, @Argument CharacterObject character) {
        return stateOps.calculateState(state, character);
    }

    @QueryMapping
    public CharacterState saveCharacterState(@Argument CharacterState characterState) {
        return states.save(characterState);
    }

    @MutationMapping 
    public CharacterDAO addEffect(@Argument String effectName, @Argument CharacterState state){
        return stateOps.calculateState(effectName, state);
    }

    @QueryMapping
    public CharacterState getCharacterStateById(@Argument String id) {
        return states.findByCharacterId(id).orElse(new CharacterState());
    }


    //pass in an empty string if you want all traits
    @QueryMapping
    public List<Trait> getTraitsList(@Argument String type){
        return type.equals("") ? traits.findAll(): traits.findByTraitType(type);
    }

    //pass in an empty string if you want all ancestries, else get by source
    @QueryMapping
    public List<Ancestry> getAncestryList(@Argument String source){
        return source.equals("") ? ancestries.findAll(): ancestries.findBySource(source);
    }

    //pass in an empty string if you want all backgrounds, else get by source
    @QueryMapping
    public List<Background> getBackgroundList(@Argument String source){
        return source.equals("") ? backgrounds.findAll(): backgrounds.findBySource(source);
    }
}
