package com.quartz.demo.dto;

import java.util.List;

import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.SendType;
import com.quartz.demo.util.enums.SimpleMisfire;
import com.quartz.demo.util.enums.TriggerType;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class QuartzTaskInformationDTO {
	private String taskId;

	private String taskName;

	List<QuartzTaskEventDTO> quartzTaskEventsList;

	QuartzTaskConfigDTO quartzTaskConfig;

	QartzTaskAnalyticsDTO qartzTaskAnalytics;

	public QuartzTaskInformationDTO() {
		quartzTaskConfig = new QuartzTaskConfigDTO();
		qartzTaskAnalytics = new QartzTaskAnalyticsDTO();
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

	public TriggerType getTriggerType() {
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
		return this.quartzTaskConfig.getRepeatCount();
	}

	public SimpleMisfire getSimpleMisfire() {
		return this.quartzTaskConfig.getSimpleMisfire();
	}

}
