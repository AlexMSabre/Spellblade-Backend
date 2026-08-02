package com.spellblade.repository;  

import org.springframework.data.mongodb.repository.MongoRepository;  
import org.springframework.stereotype.Repository;

import com.spellblade.model.Pack;

//The Repository object for the Items
//has some built in methods like findById()
//you can create some methods that will autogenerate for other fields as demonstrated below 
//(see: https://docs.spring.io/spring-data/mongodb/reference/repositories/query-methods-details.html)

@Repository
public interface PackRepository extends MongoRepository<Pack, String> {  
    Pack findByName(String name);
}