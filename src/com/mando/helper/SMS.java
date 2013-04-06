package com.mando.helper;

public class SMS {

	private String number;
	private String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public SMS(String number, String message) {
		this.number = number;
		this.message = message;
	}
}
