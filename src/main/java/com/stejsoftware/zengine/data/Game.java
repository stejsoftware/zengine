package com.stejsoftware.zengine.data;

import lombok.Builder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.File;

@Entity
@Builder
public class Game {
    @Id
    @GeneratedValue
    private String id;
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
                .statusMessage("Game Not Started")
                .gameScore(0)
                .turnsTaken(0)
                .build();
    }
}
