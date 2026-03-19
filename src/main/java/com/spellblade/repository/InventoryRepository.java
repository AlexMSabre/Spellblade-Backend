package com.spellblade.repository;  
 
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;  

import com.spellblade.model.Inventory;

//The Repository object for the Inventories
//has some built in methods like findById()
//you can create some methods that will autogenerate for other fields as demonstrated below 
//(see: https://docs.spring.io/spring-data/mongodb/reference/repositories/query-methods-details.html)


public interface InventoryRepository extends MongoRepository<Inventory, String> {  
    List<Inventory> findByCharacterId(String characterId);  
    List<Inventory> findByCharacterIdAndEquipped(String characterId, boolean equipped);
}