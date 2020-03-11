package com.quartz.demo.service;

import com.quartz.demo.dto.QuartzTaskEvent;
import com.quartz.demo.dto.QuartzTaskInformation;
import com.quartz.demo.exception.CustomSchedulerServiceException;

public interface QuartzService {
	
	QuartzTaskInformation insertNewJob(QuartzTaskInformation quartzTaskInformation);

	QuartzTaskInformation updateJob(QuartzTaskInformation quartzTaskInformation);

	QuartzTaskInformation getJobDetails(String id);
	
	boolean freezJob(String jobId) throws CustomSchedulerServiceException;
	
	boolean ScheduleJob(String jobId)throws CustomSchedulerServiceException;
	
	boolean ResumeJob(String jobId)throws CustomSchedulerServiceException;

	void recordError(QuartzTaskEvent quartzTaskError, String id);

	

}
