package com.stejsoftware.zengine.controller;

import com.stejsoftware.zengine.ZEngine;
import com.stejsoftware.zengine.data.Game;
import com.stejsoftware.zengine.data.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ZEngineRestController {
	private static final Logger LOG = LoggerFactory.getLogger(ZEngineRestController.class);

	@Autowired
	private ZEngine engine;

	@Autowired
	private ControllerConfig config;

	@GetMapping(value = "/stories")
	public List<Map<String, String>> stories(RequestEntity<String> request) {
		List<Map<String, String>> stories = new ArrayList<>();

		for (File story : Utility.getStories(config.storyFolder)) {
			stories.add(new HashMap<String, String>() {{
				put("name", story.getName());
			}});
		}

		return stories;
	}

	@GetMapping(value = "/games")
	public List<Game> listGames() {
		return engine.listGames();
	}

	@PostMapping(value = "/games")
	public Game startGame(@RequestBody() Game game) {
		return engine.startGame(game);
	}

	@GetMapping(value = "/games/{gameId}")
	public Game getGame(@PathVariable() String gameId) {
		return engine.getGame(gameId);
	}

	@GetMapping(value = "/games/{gameId}/moves")
	public List<Move> listMoves(@PathVariable String gameId) {
		return engine.listMoves(gameId);
	}

	@PostMapping(value = "/games/{gameId}/moves")
	public Move executeMove(@PathVariable String gameId, @RequestBody Move move) {
		return engine.executeMove(gameId, move);
	}

	@GetMapping(value = "/games/{gameId}/moves/{moveId}")
	public Move getMove(@PathVariable String gameId, @PathVariable String moveId) {
		return engine.getMove(gameId, moveId);
	}
}