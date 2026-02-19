package com.spellblade.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.spellblade.dbconnection.ClientCreator;
import com.spellblade.model.User;
import com.spellblade.model.UserDAO;

@Controller
public class AuthenticationController {
    @QueryMapping
    public UserDAO createOrLoginUser(@Argument UserDAO userDAO){
        User user = new User(userDAO);

        MongoClient mongoClient = ClientCreator.createClient();
        MongoDatabase spellblade = mongoClient.getDatabase("Spellblade");
        MongoCollection<User> userCol = spellblade.getCollection("USER", User.class);

        User found = userCol.find(eq("username", user.getUsername())).first();

        if(found != null){
            return new UserDAO(found);
        } else {
            userCol.insertOne(user);
            return new UserDAO(userCol.find(eq("username", user.getUsername())).first());
        }
    }
}