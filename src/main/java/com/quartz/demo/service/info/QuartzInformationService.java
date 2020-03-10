package com.quartz.demo.service.info;

import com.quartz.demo.dto.QuartzTaskEvent;
import com.quartz.demo.dto.QuartzTaskInformation;
import com.quartz.demo.util.enums.JobStatus;

public interface QuartzInformationService {

	QuartzTaskInformation insertNewJob(QuartzTaskInformation quartzTaskInformation);

	QuartzTaskInformation updateJob(QuartzTaskInformation quartzTaskInformation);

	QuartzTaskInformation getJobDetails(String id);

	void recordError(QuartzTaskEvent quartzTaskError, String id);

	void updateJobStatus(String jobId, JobStatus jobStatus);
}
