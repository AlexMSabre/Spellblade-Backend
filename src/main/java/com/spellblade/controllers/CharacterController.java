package com.spellblade.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.CharacterObject;
import com.spellblade.repository.CharacterObjectRepository;

//the endpoints for everything related to characters
@Controller
public class CharacterController {

    private final CharacterObjectRepository characters;

    public CharacterController(CharacterObjectRepository characters){
        this.characters = characters;
    }

    //creates/finds characters
    //if Id provided, find the character, else create a new one
    @QueryMapping
    public CharacterObject createOrFindCharacter(@Argument CharacterObject character) {
        return character.getId() == null ?
             characters.save(character) : 
             characters.findById(character.getId()).orElseThrow();
    }

    //See Tin
    @QueryMapping
    public List<CharacterObject> charactersByUserId(@Argument String userId) {
        return characters.findByUserId(userId);
    }
}
