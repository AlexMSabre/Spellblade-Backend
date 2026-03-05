package com.spellblade.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.spellblade.dbconnection.ClientCreator;
import com.spellblade.model.CharacterDAO;
import com.spellblade.model.CharacterObject;

@Controller
public class CharacterController {

    @QueryMapping
    public CharacterDAO createCharacter(@Argument CharacterDAO characterDAO) {
        CharacterObject character = new CharacterObject(characterDAO);

            MongoDatabase spellblade = ClientCreator.createClient();
        MongoCollection<CharacterObject> characterCol = spellblade.getCollection("CHARACTER", CharacterObject.class);
        CharacterObject found;
        if (character.getId() == null) {
            characterCol.insertOne(character);
            found = characterCol.find(eq("name", character.getName())).first();
        } else {
            found = characterCol.findOneAndReplace(eq("_id", character.getId()), character);
        }

        return new CharacterDAO(found);
    }

    @QueryMapping
    public List<CharacterDAO> charactersByAccId(@Argument String userId) {

        List<CharacterDAO> found = new ArrayList<>();

        if (userId != null) {

            MongoDatabase spellblade = ClientCreator.createClient();
            MongoCollection<CharacterObject> characterCol = spellblade.getCollection("CHARACTER", CharacterObject.class);
            characterCol.find(eq("userId", userId)).forEach((a) -> {
                System.out.println(a);
                found.add(new CharacterDAO(a));
            });
        }

        return found;
    }
}
