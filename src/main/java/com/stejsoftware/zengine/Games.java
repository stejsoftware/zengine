package com.stejsoftware.zengine;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class Games {
    private Map<String, Game> gameMap = new HashMap<>();

    public void add(Game game) {
        if (!gameMap.containsKey(game.getId())) {
            gameMap.put(game.getId(), game);
        }
    }

    public Game get(String id) {
        return gameMap.get(id);
    }

    public Set<String> list() {
        return gameMap.keySet();
    }
}
