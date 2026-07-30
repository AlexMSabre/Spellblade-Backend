package com.spellblade.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.CalculatedState;
import com.spellblade.model.CharacterObject;
import com.spellblade.operations.StateOperations;
import com.spellblade.repository.CharacterObjectRepository;
import com.spellblade.repository.EffectRepository;
import com.spellblade.repository.TalentLkpRepository;

//the endpoints for everything related to character states and effects
@Controller
public class StateController {
    private final StateOperations stateOps;

    public StateController(EffectRepository effects, TalentLkpRepository talents, CharacterObjectRepository characters){
        stateOps = new StateOperations(effects);
    }

    @QueryMapping
    public CalculatedState calculateState(@Argument CharacterObject character) {
        return stateOps.calculateState(character);
    }

    // @MutationMapping 
    // public CharacterDAO addEffect(@Argument String effectName, @Argument CharacterObject character){
    //     return stateOps.calculateState(effectName, character);
    // }
}
