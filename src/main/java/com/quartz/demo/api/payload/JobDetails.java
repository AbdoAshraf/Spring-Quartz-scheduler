package com.quartz.demo.api.payload;

import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.SendType;
import com.quartz.demo.util.enums.SimpleMisfire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class JobDetails {

	private String taskname;

	private String cornExp; //

	private SendType sendtype;

	private String url;

	private String executeparamter;

	private String triggerType;
	private int triggerPriority;
	private SimpleMisfire simpleMisfire;
	private CronMisfire cronMisfire;
	private int intervalInSeconds = 0;

}
