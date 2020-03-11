package com.quartz.demo.service.jobService;

import org.quartz.JobDataMap;

public interface JobService {
	public void call(JobDataMap jobDataMap);

}
