package com.quartz.demo.controller.payload;

import java.util.List;

import com.quartz.demo.dto.QuartzTaskError;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class JobDetailsResponse extends JobDetails{
	private Long taskId;
	private String frozenStatus;
	private Long createTime;

	private Long lastmodifyTime;

	private String sendType;

	private String url;

	private String executeParamter;
	
	private long sucssesCount;
	
	private long failCount;
	
	List<QuartzTaskErrorResponse> quartzTaskErrorsList;

}
