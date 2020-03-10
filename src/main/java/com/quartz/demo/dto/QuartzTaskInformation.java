package com.quartz.demo.dto;

import java.util.List;

import com.quartz.demo.util.enums.CronMisfire;
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

	List<QuartzTaskEvent> quartzTaskErrorsList;

	QuartzTaskConfig quartzTaskConfig;

	QartzTaskAnalytics qartzTaskAnalytics;

	public QuartzTaskInformation() {
		qartzTaskAnalytics = new QartzTaskAnalytics();
		quartzTaskConfig = new QuartzTaskConfig();
	}

	public SendType getSendType() {
		return this.quartzTaskConfig.getSendType();
	}

	public String getUrl() {
		return this.quartzTaskConfig.getUrl();
	}

	public String getCornExp() {
		return this.quartzTaskConfig.getCornExp();
	}

	public String getExecuteParamter() {
		return this.quartzTaskConfig.getExecuteParamter();
	}

	public String getTriggerType() {
		return this.quartzTaskConfig.getTriggerType();
	}

	public CronMisfire getCronMisfire() {
		return this.quartzTaskConfig.getCronMisfire();
	}

	public int getIntervalInSeconds() {
		return this.quartzTaskConfig.getIntervalInSeconds();
	}

	public int getTriggerPriority() {
		return this.quartzTaskConfig.getTriggerPriority();
	}

	public int getRepeatCount() {
		return this.getRepeatCount();
	}

	public SimpleMisfire getSimpleMisfire() {
		return this.quartzTaskConfig.getSimpleMisfire();
	}

}
