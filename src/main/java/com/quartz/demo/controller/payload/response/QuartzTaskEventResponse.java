package com.quartz.demo.controller.payload.response;

import java.time.LocalDateTime;

import com.quartz.demo.util.enums.EventType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuartzTaskEventResponse {
	private String errorId;

	private EventType eventType;

	private LocalDateTime executeTime;

	private String cause;
}