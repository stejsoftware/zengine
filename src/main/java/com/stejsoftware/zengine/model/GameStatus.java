package com.stejsoftware.zengine.model;

import lombok.Builder;

@Builder
public class GameStatus {
	private String statusMessage;
	private Integer gameScore;
	private Integer turnsTaken;
}
