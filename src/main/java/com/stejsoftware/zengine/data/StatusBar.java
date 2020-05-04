package com.stejsoftware.zengine.data;

public class StatusBar {
    private final String message;

    private final int score;

    private final int turns;

    private final boolean flag;

    public StatusBar() {
        this.message = "";
        this.score = 0;
        this.turns = 0;
        this.flag = false;
    }

    public StatusBar(String message, int score, int turns, boolean flag) {
        this.message = message;
        this.score = score;
        this.turns = turns;
        this.flag = flag;
    }

    public String getMessage() {
        return this.message;
    }

    public int getScore() {
        return this.score;
    }

    public int getTurns() {
        return this.turns;
    }

    public boolean isFlag() {
        return this.flag;
    }

    public boolean getFlag() {
        return this.flag;
    }

}