package com.spellblade.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.CalculatedState;
import com.spellblade.model.CharacterObject;
import com.spellblade.model.CharacterState;
import com.spellblade.model.dao.CharacterDAO;
import com.spellblade.operations.StateOperations;
import com.spellblade.repository.CharacterObjectRepository;
import com.spellblade.repository.CharacterStateRepository;
import com.spellblade.repository.EffectRepository;
import com.spellblade.repository.TalentLkpRepository;

//the endpoints for everything related to character states and effects
@Controller
public class StateController {

    @Autowired
    private CharacterStateRepository states;
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

    @QueryMapping 
    public CharacterDAO addEffect(@Argument String effectName, @Argument CharacterState state){
        return stateOps.calculateState(effectName, state);
    }

    @QueryMapping
    public CharacterState getCharacterStateById(@Argument String id) {
        return states.findByCharacterId(id).orElse(new CharacterState());
    }
}
