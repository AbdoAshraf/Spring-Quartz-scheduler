package com.quartz.demo.service.scheduler;

import com.quartz.demo.dto.QuartzTaskInformation;

public interface QuartzSchedulerService {
	public boolean scheduleJob(QuartzTaskInformation quartzTaskInformation);

	boolean UnscheduleJob(QuartzTaskInformation quartzTaskInformation);

	boolean pausejob(QuartzTaskInformation quartzTaskInformation);

	boolean resumeJob(QuartzTaskInformation quartzTaskInformation);
}
