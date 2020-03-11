package com.quartz.demo.dto;

import com.quartz.demo.stream.TriggerType;
import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.SendType;
import com.quartz.demo.util.enums.SimpleMisfire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuartzTaskConfig {

	private SendType sendType;

	private String url;

	private String cornExp;

	private String executeParamter;

	private TriggerType triggerType;

	private int triggerPriority;

	private int repeatCount;

	private SimpleMisfire simpleMisfire;

	private CronMisfire cronMisfire;

	private int intervalInSeconds = 0;

}
