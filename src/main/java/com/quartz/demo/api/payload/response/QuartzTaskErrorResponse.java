package com.quartz.demo.api.payload.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuartzTaskErrorResponse {
	private LocalDateTime executeTime;
	private String cause;
}
