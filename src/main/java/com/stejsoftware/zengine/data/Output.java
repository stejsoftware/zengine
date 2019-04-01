package com.stejsoftware.zengine.data;

public class Output {
	String message = null;
	String error = null;
	Object data = null;

	private Output() {

	}

	public static Output ok(String message, Object data) {
		Output output = new Output();
		output.setMessage(message);
		output.setData(data);
		return output;
	}

	public static Output error(String message) {
		Output output = new Output();
		output.setError(message);
		return output;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
