package com.spellblade.repository;  
 
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;  

import com.spellblade.model.PartyInventory;

//The Repository object for the Items
//has some built in methods like findById()
//you can create some methods that will autogenerate for other fields as demonstrated below 
//(see: https://docs.spring.io/spring-data/mongodb/reference/repositories/query-methods-details.html)


public interface PartyInventoryRepository extends MongoRepository<PartyInventory, String> {  
    List<PartyInventory> findByGameId(String source);
}