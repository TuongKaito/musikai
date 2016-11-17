package com.kaito.musiconline.exceptions;

public class ResourceError {

	private int code;
	private String message;
	
	public ResourceError(String message) {
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
