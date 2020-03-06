package com.quartz.demo.exception;


public class InfoServiceExcseption extends RuntimeException{
 
	private static final long serialVersionUID = 1348771109171435607L;

	public InfoServiceExcseption(String message)
	{
		super(message);
	}
}