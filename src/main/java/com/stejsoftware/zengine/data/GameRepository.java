package com.stejsoftware.zengine.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, Integer> {
    Game findByName(String name);
}
