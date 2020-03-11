package com.quartz.demo.service;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quartz.demo.dto.QuartzTaskEvent;
import com.quartz.demo.dto.QuartzTaskInformation;
import com.quartz.demo.exception.CustomSchedulerServiceException;
import com.quartz.demo.exception.ValidationException;
import com.quartz.demo.service.info.QuartzInformationService;
import com.quartz.demo.service.scheduler.QuartzSchedulerService;
import com.quartz.demo.util.enums.SendType;
import com.quartz.demo.util.enums.TriggerType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
		this.validate(quartzTaskInformation);
		return this.quartzInformationService.insertNewJob(quartzTaskInformation);
	}

	private void validate(QuartzTaskInformation quartzTaskInformation) throws ValidationException {
		if (quartzTaskInformation.getSendType() == SendType.URL && quartzTaskInformation.getUrl() == null) {
			throw new ValidationException("messing url");
		}
		if (quartzTaskInformation.getTriggerType() == TriggerType.CORN && quartzTaskInformation.getCornExp() == null) {
			throw new ValidationException("messing corn exp");
		}
		if (quartzTaskInformation.getTriggerType() == TriggerType.Simple
				&& quartzTaskInformation.getIntervalInSeconds() == 0) {
			throw new ValidationException("messing time interval");
		}
	}

	@Override
	public QuartzTaskInformation updateJob(QuartzTaskInformation quartzTaskInformation) {
		return this.quartzInformationService.updateJob(quartzTaskInformation);
	}

	@Override
	public QuartzTaskInformation getJobDetails(String id) {
		return this.quartzInformationService.getJobDetails(id);
	}

	@Override
	public boolean freezJob(String jobId) throws CustomSchedulerServiceException {
		QuartzTaskInformation quartzTaskInformation = this.quartzInformationService.getJobDetails(jobId);
		try {
			this.quartzSchedulerService.pausejob(quartzTaskInformation);
		} catch (SchedulerException e) {
			throw new CustomSchedulerServiceException(e.getMessage(), e, true, true);
		}
		// this.quartzInformationService.updateJobStatus(jobId, JobStatus.FROZEN);
		return true;
	}

	@Override
	public boolean ScheduleJob(String jobId) throws CustomSchedulerServiceException {
		QuartzTaskInformation quartzTaskInformation = this.quartzInformationService.getJobDetails(jobId);
		try {
			this.quartzSchedulerService.scheduleJob(quartzTaskInformation);
		} catch (SchedulerException e) {
			throw new CustomSchedulerServiceException(e.getMessage(), e, true, true);
		}
		// this.quartzInformationService.updateJobStatus(jobId, JobStatus.UNFROZEN);
		return true;

	}

	@Override
	public boolean ResumeJob(String jobId) throws CustomSchedulerServiceException {
		QuartzTaskInformation quartzTaskInformation = this.quartzInformationService.getJobDetails(jobId);
		try {
			this.quartzSchedulerService.resumeJob(quartzTaskInformation);
		} catch (SchedulerException e) {
			throw new CustomSchedulerServiceException(e.getMessage(), e, true, true);

		}
		return true;
	}

	@Override
	public void recordError(QuartzTaskEvent quartzTaskError, String id) {
		this.quartzInformationService.recordError(quartzTaskError, id);
	}

}
