package com.spellblade.model;

import lombok.Data;

@Data
public class UserDAO {

    String id;
    String username;
    String email;

    public UserDAO(){}

    public UserDAO(String username, String email){
        this.username = username;
        this.email = email;
    }

    public UserDAO(User user){
        this.id = user.getId().toString();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}