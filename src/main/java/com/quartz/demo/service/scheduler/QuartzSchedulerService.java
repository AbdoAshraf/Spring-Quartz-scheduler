package com.quartz.demo.service.scheduler;

import com.quartz.demo.dto.QuartzTaskInformationDTO;
import com.quartz.demo.exception.CustomSchedulerServiceException;

public interface QuartzSchedulerService {
	public boolean scheduleJob(QuartzTaskInformationDTO quartzTaskInformationDTO) throws CustomSchedulerServiceException;

	boolean UnscheduleJob(String jobId) throws CustomSchedulerServiceException;

	boolean pausejob(String jobId) throws CustomSchedulerServiceException;

	boolean resumeJob(String jobId) throws CustomSchedulerServiceException;
}
