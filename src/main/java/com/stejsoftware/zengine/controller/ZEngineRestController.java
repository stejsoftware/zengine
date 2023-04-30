package com.stejsoftware.zengine.controller;

import com.stejsoftware.zengine.Utility;
import com.stejsoftware.zengine.ZEngine;
import com.stejsoftware.zengine.data.Game;
import com.stejsoftware.zengine.model.Move;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v0")
public class ZEngineRestController {
    private final ZEngine engine;
    private final ControllerConfig config;

    public ZEngineRestController(ZEngine engine, ControllerConfig config) {
        this.engine = engine;
        this.config = config;
    }

    @GetMapping(value = "/stories")
    public List<Map<String, String>> stories(RequestEntity<String> request) {
        List<Map<String, String>> stories = new ArrayList<>();

        for (File story : Utility.getStories(config.getStoryFolder())) {
            stories.add(Map.of("name", story.getName()));
        }

        return stories;
    }

    @GetMapping(value = "/games")
    public List<Game> listGames() {
        return engine.listGames();
    }

    @PostMapping(value = "/games")
    public Game startGame(@RequestBody Game game) {
        return engine.startGame(game);
    }

    @GetMapping(value = "/games/{gameId}")
    public Game getGame(@PathVariable String gameId) {
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