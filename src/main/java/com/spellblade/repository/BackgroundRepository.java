package com.spellblade.repository;  
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spellblade.model.Background;


//has some built in methods like findById()
//you can create some methods that will autogenerate for other fields as demonstrated below 
//(see: https://docs.spring.io/spring-data/mongodb/reference/repositories/query-methods-details.html)


public interface BackgroundRepository extends MongoRepository<Background, String> {  
    Optional<Background> findByName(String name);
    List<Background> findBySource(String source);
}