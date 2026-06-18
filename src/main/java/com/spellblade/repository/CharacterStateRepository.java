package com.spellblade.repository;  

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;  

import com.spellblade.model.CharacterState;

//The Repository object for the CharacterObject
//has some built in methods like findById()
//you can create some methods that will autogenerate for other fields as demonstrated below 
//(see: https://docs.spring.io/spring-data/mongodb/reference/repositories/query-methods-details.html)


public interface CharacterStateRepository extends MongoRepository<CharacterState, String> {  
    Optional<CharacterState> findByCharacterId(String id);
}