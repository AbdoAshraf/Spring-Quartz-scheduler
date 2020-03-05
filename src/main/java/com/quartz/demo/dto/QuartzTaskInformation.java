package com.quartz.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.quartz.demo.util.enums.JobStatus;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class QuartzTaskInformation {
	private String taskId;
	
	private String taskName;

	private String cornExp;

	private JobStatus jobStatus;

	private LocalDateTime frozenTime;

	private LocalDateTime unfrozenTime;

	private LocalDateTime createTime;

	private LocalDateTime lastmodifyTime;

	private String sendType;

	private String url;

	private String executeParamter;
	
	private long sucssesCount;
	
	private long failCount;
	
	List<QuartzTaskError> quartzTaskErrorsList;

}