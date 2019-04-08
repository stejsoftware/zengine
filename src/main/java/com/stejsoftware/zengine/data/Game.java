package com.stejsoftware.zengine.data;

import java.io.File;

public class Game {
	private final String id;
	private final File storyFile;

	private String name;
	private String statusMessage;
	private Integer gameScore;
	private Integer turnsTaken;

	public Game(String id, File storyFile) {
		this.id = id;
		this.storyFile = storyFile;

		this.name = storyFile.getName();
		this.statusMessage = "Game Not Started";
		this.gameScore = 0;
		this.turnsTaken = 0;
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
