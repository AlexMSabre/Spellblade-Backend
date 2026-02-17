package com.spellblade.endpoints;

import org.bson.codecs.configuration.CodecProvider;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.spellblade.model.Character;

@Controller
@CrossOrigin(origins = "*")
public class CharacterController {
    @QueryMapping
    public Character createCharacter(@Argument Character character) {    
        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");

        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .codecRegistry(codecRegistry)
                    .build();

        MongoClient mongoClient = MongoClients.create(clientSettings);
        MongoDatabase spellblade = mongoClient.getDatabase("Spellblade").withCodecRegistry(codecRegistry);
        MongoCollection<Character> characterCol = spellblade.getCollection("CHARACTER", Character.class);
        Character found;
        if(character.getId() == null){
            characterCol.insertOne(character);
		    found = characterCol.find(eq("name", character.getName())).first();
        } else {
            found = characterCol.findOneAndReplace(eq("_id", character.getId()), character);
        }

        System.out.println(found);

        return found;
    }
}