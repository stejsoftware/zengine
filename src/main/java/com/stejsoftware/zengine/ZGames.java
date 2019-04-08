package com.stejsoftware.zengine;

import com.stejsoftware.zengine.data.Game;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZGames {
	private Map<String, ZGame> gameMap = new HashMap<>();

	public ZGame add(ZGame game) {
		if (!gameMap.containsKey(game.getId())) {
			gameMap.put(game.getId(), game);
		}

		return get(game.getId());
	}

	public ZGame get(String id) {
		return gameMap.get(id);
	}

	public List<Game> list() {
		List<Game> list = new ArrayList<>();

		for (ZGame game : gameMap.values()) {
			list.add(game.toGame());
		}

		return list;
	}
}
