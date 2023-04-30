package com.stejsoftware.zengine.model;

import lombok.Builder;

@Builder
public class Output {
	String message;
	String error;
	Object data;

	public static Output ok(String message, Object data) {
		return Output.builder()
			.message(message)
			.data(data)
			.build();
	}

	public static Output error(String message) {
		return Output.builder()
			.error(message)
			.build();
	}
}
