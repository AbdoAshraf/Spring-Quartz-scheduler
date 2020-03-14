package com.quartz.demo.service.info;

import com.quartz.demo.dto.QuartzTaskConfigDTO;
import com.quartz.demo.dto.QuartzTaskEventDTO;
import com.quartz.demo.dto.QuartzTaskInformationDTO;
import com.quartz.demo.util.enums.JobStatus;

public interface QuartzInformationService {

	QuartzTaskInformationDTO insertNewJob(QuartzTaskInformationDTO quartzTaskInformationDTO);

	QuartzTaskInformationDTO getJobDetails(String id);

	void recordEvent(QuartzTaskEventDTO quartzTaskError, String id);

	void updateJobStatus(String taskId, JobStatus jobStatus);

	void updateJobConfig(String taskId, QuartzTaskConfigDTO quartzTaskConfigDTO);

}
