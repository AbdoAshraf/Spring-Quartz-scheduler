package com.quartz.demo.service;

import com.quartz.demo.dto.QuartzTaskConfigDTO;
import com.quartz.demo.dto.QuartzTaskEventDTO;
import com.quartz.demo.dto.QuartzTaskInformationDTO;
import com.quartz.demo.exception.CustomSchedulerServiceException;

public interface QuartzService {

	QuartzTaskInformationDTO insertNewJob(QuartzTaskInformationDTO quartzTaskInformationDTO);

	QuartzTaskInformationDTO getJobDetails(String id);

	boolean freezJob(String jobId) throws CustomSchedulerServiceException;

	boolean ScheduleJob(String jobId) throws CustomSchedulerServiceException;

	boolean ResumeJob(String jobId) throws CustomSchedulerServiceException;

	void recordError(QuartzTaskEventDTO quartzTaskError, String id);

	void updateJobConfig(String taskId, QuartzTaskConfigDTO quartzTaskConfigDTO);

}
