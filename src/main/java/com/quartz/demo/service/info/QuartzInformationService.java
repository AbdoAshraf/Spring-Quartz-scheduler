package com.quartz.demo.service.info;

import com.quartz.demo.dto.QuartzTaskInformation;

public interface QuartzInformationService {

	QuartzTaskInformation insertNewJob(QuartzTaskInformation quartzTaskInformation);

	QuartzTaskInformation updateJob(QuartzTaskInformation quartzTaskInformation);

	QuartzTaskInformation getJobDetails(String id);

}
