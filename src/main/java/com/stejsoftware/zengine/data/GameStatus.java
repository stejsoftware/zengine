package com.stejsoftware.zengine.data;

public class GameStatus {
    private String statusMessage;
    private Integer gameScore;
    private Integer turnsTaken;

    GameStatus() {
        this.statusMessage = "";
        this.gameScore = 0;
        this.turnsTaken = 0;
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Integer getGameScore() {
        return gameScore;
    }

    public void setGameScore(Integer gameScore) {
        this.gameScore = gameScore;
    }

    public Integer getTurnsTaken() {
        return turnsTaken;
    }

    public void setTurnsTaken(Integer turnsTaken) {
        this.turnsTaken = turnsTaken;
    }
}
