package com.stejsoftware.zengine.processor;

import com.stejsoftware.zengine.data.Game;
import org.springframework.stereotype.Component;


@Component
public class CPU {
    public String execute(Game game) {
        return execute(game, "");
    }

    public String execute(Game game, String input) {
        return "Unknown Command";
    }
}
