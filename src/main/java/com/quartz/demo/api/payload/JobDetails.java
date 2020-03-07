package com.quartz.demo.api.payload;

import javax.validation.constraints.NotNull;

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

	@NotNull
	private String taskname;

	
	private String cornExp; //

	@NotNull
	private SendType sendtype;

	private String url;
	
	private String executeparamter;
	
	@NotNull
	private String triggerType;

	private int triggerPriority;

	private SimpleMisfire simpleMisfire;

	private CronMisfire cronMisfire;

	private int intervalInSeconds = 0;
	
    private int repeatCount = 0;


}
