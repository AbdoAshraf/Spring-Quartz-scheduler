package com.quartz.demo.exception;

import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.quartz.demo.job.DummyJob;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice("com.quartz.demo.api.SchedulerController")
@Slf4j
public class AppExceptionsHandler {

	@ExceptionHandler(value = { CustomSchedulerServiceException.class })
	public ResponseEntity<Object> handleUserServiceException(CustomSchedulerServiceException ex, WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
		log.error(ex.getMessage(), ex);
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	@ExceptionHandler(value = { InfoServiceExcseption.class })
	public ResponseEntity<Object> handleUserServiceException(InfoServiceExcseption ex, WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
		log.error(ex.getMessage(), ex);
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
