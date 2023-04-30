package com.stejsoftware.zengine.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Move {
	private String input;
	private String output;
}
