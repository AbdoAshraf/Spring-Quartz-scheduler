package com.quartz.demo.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class QuartzTaskInformations {
	private Long taskId;
	
	private String taskName;

	private String cornExp;

	private String frozenStatus;

	private Long frozenTime;

	private Long unfrozenTime;

	private Long createTime;

	private Long lastmodifyTime;

	private String sendType;

	private String url;

	private String executeParamter;
	
	private long sucssesCount;
	
	private long failCount;
	
	List<QuartzTaskError> quartzTaskErrorsList;

}