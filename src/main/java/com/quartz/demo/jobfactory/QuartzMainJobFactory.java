package com.quartz.demo.jobfactory;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.quartz.demo.service.jobService.JobService;

import lombok.extern.slf4j.Slf4j;

@DisallowConcurrentExecution
@Slf4j
public class QuartzMainJobFactory implements Job {

	@Autowired
	private JobService jobService;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
		log.info("Executing Now:taskName={},sendType={},url={}", jobDataMap.getString("name"),
				jobDataMap.getString("sendType"));
		jobService.call(jobDataMap);
	}

}
