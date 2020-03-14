package com.quartz.demo.controller.payload;

import javax.validation.constraints.NotNull;

import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.SendType;
import com.quartz.demo.util.enums.SimpleMisfire;
import com.quartz.demo.util.enums.TriggerType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuartzTaskConfig {
	@NotNull
	private SendType sendType;
	@NotNull
	private String url;
	@NotNull
	private String cornExp;

	private String executeParamter;

	private TriggerType triggerType;

	private int triggerPriority;

	private int repeatCount;

	private SimpleMisfire simpleMisfire;

	private CronMisfire cronMisfire;

	private int intervalInSeconds = 0;

}
