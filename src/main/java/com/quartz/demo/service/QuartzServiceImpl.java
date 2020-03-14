package com.quartz.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quartz.demo.dto.QuartzTaskConfigDTO;
import com.quartz.demo.dto.QuartzTaskEventDTO;
import com.quartz.demo.dto.QuartzTaskInformationDTO;
import com.quartz.demo.exception.CustomSchedulerServiceException;
import com.quartz.demo.exception.ValidationException;
import com.quartz.demo.service.info.QuartzInformationService;
import com.quartz.demo.service.scheduler.QuartzSchedulerService;
import com.quartz.demo.util.enums.JobStatus;
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
	public QuartzTaskInformationDTO insertNewJob(QuartzTaskInformationDTO quartzTaskInformationDTO) {
		this.validate(quartzTaskInformationDTO.getQuartzTaskConfig());
		return this.quartzInformationService.insertNewJob(quartzTaskInformationDTO);
	}

	@Override
	public void updateJobConfig(String taskId, QuartzTaskConfigDTO quartzTaskConfigDTO) {
		this.validate(quartzTaskConfigDTO);
		this.quartzInformationService.updateJobConfig(taskId, quartzTaskConfigDTO);
		this.quartzSchedulerService.UnscheduleJob(taskId);
		this.quartzSchedulerService.scheduleJob(this.getJobDetails(taskId));
	}

	private void validate(QuartzTaskConfigDTO quartzTaskInformation) throws ValidationException {
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
	public QuartzTaskInformationDTO getJobDetails(String id) {
		return this.quartzInformationService.getJobDetails(id);
	}

	@Override
	public boolean freezJob(String jobId) throws CustomSchedulerServiceException {
		QuartzTaskInformationDTO quartzTaskInformationDTO = this.quartzInformationService.getJobDetails(jobId);
		try {
			this.quartzSchedulerService.pausejob(quartzTaskInformationDTO.getTaskId());
		} catch (CustomSchedulerServiceException e) {
			throw e;
		}
		this.quartzInformationService.updateJobStatus(jobId, JobStatus.FROZEN);
		return true;
	}

	@Override
	public boolean ScheduleJob(String jobId) throws CustomSchedulerServiceException {
		QuartzTaskInformationDTO quartzTaskInformationDTO = this.quartzInformationService.getJobDetails(jobId);
		try {
			this.quartzSchedulerService.scheduleJob(quartzTaskInformationDTO);
		} catch (CustomSchedulerServiceException e) {
			throw e;
		}
		this.quartzInformationService.updateJobStatus(jobId, JobStatus.UNFROZEN);
		return true;

	}

	@Override
	public boolean ResumeJob(String jobId) throws CustomSchedulerServiceException {
		QuartzTaskInformationDTO quartzTaskInformationDTO = this.quartzInformationService.getJobDetails(jobId);
		try {
			this.quartzSchedulerService.resumeJob(quartzTaskInformationDTO.getTaskId());
		} catch (CustomSchedulerServiceException e) {
			throw e;

		}
		this.quartzInformationService.updateJobStatus(jobId, JobStatus.UNFROZEN);
		return true;
	}

	@Override
	public void recordError(QuartzTaskEventDTO quartzTaskError, String id) {
		this.quartzInformationService.recordEvent(quartzTaskError, id);
	}

}
