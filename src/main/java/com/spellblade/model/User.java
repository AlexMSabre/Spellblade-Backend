package com.spellblade.model;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
public class User {

    ObjectId id;
    String username;
    String email;

    public User(){}

    public User(String username, String email){
        this.username = username;
        this.email = email;
    }

    public User(UserDAO user){
        if(user.getId()!=null)
            this.id = new ObjectId(user.getId());
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}