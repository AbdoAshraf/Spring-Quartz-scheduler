package com.quartz.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.JobStatus;
import com.quartz.demo.util.enums.SendType;
import com.quartz.demo.util.enums.SimpleMisfire;

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

	private SendType sendType;
	
	private String triggerType;
	
	private int triggerPriority;
	
	private SimpleMisfire simpleMisfire;
	
	private CronMisfire cronMisfire;
	
	private int intervalInSeconds = 0;
	
	private int repeatCount=0;
	
	private String url;

	private String executeParamter;
	
	private long sucssesCount;
	
	private long failCount;
	
	List<QuartzTaskError> quartzTaskErrorsList;

}