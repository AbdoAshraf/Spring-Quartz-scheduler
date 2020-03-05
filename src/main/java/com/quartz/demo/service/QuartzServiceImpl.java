package com.quartz.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quartz.demo.dto.QuartzTaskInformation;
import com.quartz.demo.service.info.QuartzInformationService;
import com.quartz.demo.service.scheduler.QuartzSchedulerService;

@Service
public class QuartzServiceImpl implements QuartzService {
	private QuartzInformationService quartzInformationService;
	private QuartzSchedulerService quartzSchedulerService;

	@Autowired
	public QuartzServiceImpl(QuartzInformationService quartzInformationService,
			QuartzSchedulerService quartzSchedulerService) {
		this.quartzInformationService = quartzInformationService;
		this.quartzSchedulerService = quartzSchedulerService;
	}

	@Override
	public QuartzTaskInformation insertNewJob(QuartzTaskInformation quartzTaskInformation) {
		return this.quartzInformationService.insertNewJob(quartzTaskInformation);
	}

	@Override
	public QuartzTaskInformation updateJob(QuartzTaskInformation schedulejobRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuartzTaskInformation getJobDetails(String id) {
		this.quartzInformationService.getJobDetails(id);
		return null;
	}

}
