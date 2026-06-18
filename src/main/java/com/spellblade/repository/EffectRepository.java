package com.spellblade.repository;  
 
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spellblade.model.Effect;


//The Repository object for the Effects
//has some built in methods like findById()
//you can create some methods that will autogenerate for other fields as demonstrated below 
//(see: https://docs.spring.io/spring-data/mongodb/reference/repositories/query-methods-details.html)


public interface EffectRepository extends MongoRepository<Effect, String> {  
    Effect findByName(String name);
    List<Effect> findByNameContainingIgnoreCase(String name);
    List<Effect> findByEffectType(int Type);
}