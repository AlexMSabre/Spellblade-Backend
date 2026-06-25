package com.spellblade.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        //MongoDB connection string here
        return MongoClients.create("mongodb+srv://spellbladettrpg_db_user:vxZraFARoiYTnPxx@spellblade0.hs5zsn5.mongodb.net/?appName=Spellblade0"); 
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        // Specify the default database name
        return new MongoTemplate(mongoClient(), "SPELLBLADE_DEV");
    }
}