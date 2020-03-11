package com.quartz.demo.exception;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1348771109171435607L;

	public ValidationException(String message) {
		super(message);
	}
}
