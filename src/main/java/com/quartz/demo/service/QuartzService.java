package com.quartz.demo.service;

import com.quartz.demo.dto.QuartzTaskInformation;

public interface QuartzService {
	
	QuartzTaskInformation insertNewJob(QuartzTaskInformation quartzTaskInformation);

	QuartzTaskInformation updateJob(QuartzTaskInformation quartzTaskInformation);

	QuartzTaskInformation getJobDetails(String id);

}
