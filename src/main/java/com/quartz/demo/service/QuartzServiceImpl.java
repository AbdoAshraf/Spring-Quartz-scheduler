package com.quartz.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quartz.demo.dto.QuartzTaskError;
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
	public QuartzTaskInformation updateJob(QuartzTaskInformation quartzTaskInformation) {
		return this.quartzInformationService.updateJob(quartzTaskInformation);
	}

	@Override
	public QuartzTaskInformation getJobDetails(String id) {
		this.quartzInformationService.getJobDetails(id);
		return null;
	}

	@Override
	public boolean freezJob(String jobId) {
		QuartzTaskInformation quartzTaskInformation = this.quartzInformationService.getJobDetails(jobId);
		try {
			this.quartzSchedulerService.pausejob(quartzTaskInformation);
		} catch (Exception e) {

		}
		return true;
	}

	@Override
	public boolean ScheduleJob(String jobId) {
		QuartzTaskInformation quartzTaskInformation = this.quartzInformationService.getJobDetails(jobId);
		try {
			this.quartzSchedulerService.scheduleJob(quartzTaskInformation);
			quartzTaskInformation.setLastmodifyTime(LocalDateTime.now());
			quartzTaskInformation.setUnfrozenTime(LocalDateTime.now());
		} catch (Exception e) {

		}
		return true;

	}

	@Override
	public boolean ResumeJob(String jobId) {
		QuartzTaskInformation quartzTaskInformation = this.quartzInformationService.getJobDetails(jobId);
		try {
			this.quartzSchedulerService.resumeJob(quartzTaskInformation);
		} catch (Exception e) {

		}
		return true;
	}

	public void recordError(Exception e, String id) {
		QuartzTaskError quartzTaskError = new QuartzTaskError();
		quartzTaskError.setExecuteTime(LocalDateTime.now());
		quartzTaskError.setFailReason(e.getMessage());

	}

}
