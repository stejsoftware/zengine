package com.stejsoftware.zengine.data;

import lombok.Builder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.File;
import java.util.Objects;

@Entity
@Builder
public class Game {
    private @Id
    @GeneratedValue String id;
    private File storyFile;
    private String name;
    private String statusMessage;
    private Integer gameScore;
    private Integer turnsTaken;

    public static Game createGame(String id, File storyFile) {
        return Game.builder()
                .id(id)
                .storyFile(storyFile)
                .name(storyFile.getName())
                .statusMessage("0Game Not Started")
                .gameScore(0)
                .turnsTaken(0)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) &&
                Objects.equals(name, game.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public String getId() {
        return id;
    }

    public File getStoryFile() {
        return storyFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
