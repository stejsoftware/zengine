package com.stejsoftware.zengine.data;


public interface GameRepository {
    Game findByName(String name);
}
