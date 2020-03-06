package com.quartz.demo.exception;

public class CustomSchedulerServiceException extends RuntimeException {
	private static final long serialVersionUID = 8624944628363400977L;

	public CustomSchedulerServiceException() {
		super();
	}

	public CustomSchedulerServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CustomSchedulerServiceException(String message) {
		super(message);
	}

	public CustomSchedulerServiceException(Throwable cause) {
		super(cause);
	}

}