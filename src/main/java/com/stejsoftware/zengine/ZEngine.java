package com.stejsoftware.zengine;

import com.stejsoftware.zengine.controller.ControllerConfig;
import com.stejsoftware.zengine.data.Game;
import com.stejsoftware.zengine.data.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ZEngine {

	private static final Logger LOG = LoggerFactory.getLogger(ZEngine.class);

	@Autowired
	private ZGames games;

	@Autowired
	private ControllerConfig config;

	public Game start(File story) {
		ZGame game = new ZGame(story);
		games.add(game);
		return game.toGame();
	}

	public List<Game> listGames() {
		return games.list();
	}

	public Game getGame(String id) {
		return games.get(id).toGame();
	}

	public Game startGame(Game game) {
		Game newGame = new Game();

		try {
			File story = new ClassPathResource(config.storyFolder + "/" + game.getStoryFile()).getFile();
			game.init(story);
		}
		catch (IOException e) {
			LOG.error("error", e);
		}

		return newGame;
	}

	public Move executeMove(String gameId, Move move) {
		return move;
	}

	public List<Move> listMoves(String gameId) {
		return new ArrayList<>();
	}

	public Move getMove(String gameId, String moveId) {
		return new Move();
	}
}