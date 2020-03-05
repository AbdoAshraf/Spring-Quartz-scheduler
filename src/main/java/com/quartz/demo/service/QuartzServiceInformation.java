package com.quartz.demo.service;

import com.quartz.demo.dto.QuartzTaskInformation;

public interface QuartzServiceInformation {

	QuartzTaskInformation insertNewJob(QuartzTaskInformation schedulejobRequest);

	QuartzTaskInformation updateJob(QuartzTaskInformation schedulejobRequest);

	QuartzTaskInformation getJobDetails(String id);

}
