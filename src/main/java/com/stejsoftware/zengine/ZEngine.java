package com.stejsoftware.zengine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ZEngine {

    @Autowired
    public Games games;

    public Game start(File story) {
        Game game = new Game(story);

        games.add(game);

        return game;
    }

    public List<String> gameList() {
        List<String> list = new ArrayList<>();
        list.addAll(games.list());

        return list;
    }
}