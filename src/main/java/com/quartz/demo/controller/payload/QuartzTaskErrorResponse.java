package com.quartz.demo.controller.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuartzTaskErrorResponse {
	private Long executeTime;

	private String failReason;

	private Long lastModifytime;

}
