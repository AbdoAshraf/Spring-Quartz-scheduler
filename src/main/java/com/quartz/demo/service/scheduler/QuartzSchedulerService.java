package com.quartz.demo.service.scheduler;

import org.quartz.SchedulerException;

import com.quartz.demo.dto.QuartzTaskInformation;

public interface QuartzSchedulerService {
	public boolean scheduleJob(QuartzTaskInformation quartzTaskInformation) throws SchedulerException ;

	boolean UnscheduleJob(QuartzTaskInformation quartzTaskInformation)throws SchedulerException;

	boolean pausejob(QuartzTaskInformation quartzTaskInformation) throws SchedulerException;

	boolean resumeJob(QuartzTaskInformation quartzTaskInformation)throws SchedulerException;
}
