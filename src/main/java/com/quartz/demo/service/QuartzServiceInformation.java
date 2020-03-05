package com.quartz.demo.service;

import com.quartz.demo.dto.QuartzTaskInformations;

public interface QuartzServiceInformation {

	QuartzTaskInformations insertNewJob(QuartzTaskInformations schedulejobRequest);

	QuartzTaskInformations updateJob(QuartzTaskInformations schedulejobRequest);

	QuartzTaskInformations getJobDetails(String id);

}
