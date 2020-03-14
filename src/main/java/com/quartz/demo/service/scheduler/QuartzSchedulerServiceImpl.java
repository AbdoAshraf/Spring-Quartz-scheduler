package com.quartz.demo.service.scheduler;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quartz.demo.dto.QuartzTaskInformationDTO;
import com.quartz.demo.exception.CustomSchedulerServiceException;
import com.quartz.demo.jobfactory.QuartzMainJobFactory;
import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.SimpleMisfire;
import com.quartz.demo.util.enums.TriggerType;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@NoArgsConstructor
public class QuartzSchedulerServiceImpl implements QuartzSchedulerService {

	private Scheduler scheduler;

	@Autowired
	public QuartzSchedulerServiceImpl(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public boolean scheduleJob(QuartzTaskInformationDTO quartzTaskInformationDTO) throws CustomSchedulerServiceException {
		try {
			this.addJob(quartzTaskInformationDTO);
		} catch (SchedulerException e) {
			throw new CustomSchedulerServiceException(e.getMessage(), e, true, true);
		}
		return true;
	}

	private void addJob(QuartzTaskInformationDTO quartzTaskInformationDTO) throws SchedulerException {
		String jobGroup = Scheduler.DEFAULT_GROUP;
		TriggerKey triggerKey = TriggerKey.triggerKey(quartzTaskInformationDTO.getTaskId(), jobGroup);
		if (checkExists(triggerKey)) {
			throw new CustomSchedulerServiceException(
					String.format("Job already active", quartzTaskInformationDTO.getTaskName(), jobGroup));
		}
		JobDetail jobDetail = buildJob(quartzTaskInformationDTO);
		try {
			Trigger trigger = this.selectTrigger(triggerKey, quartzTaskInformationDTO);
			scheduler.scheduleJob(jobDetail, trigger);
			String schedulerName = scheduler.getSchedulerName();
			log.info("schedulerName:{},jobName:{},jobGroup:{},jobClass:{}", schedulerName,
					quartzTaskInformationDTO.getTaskName(), jobGroup);
		} catch (SchedulerException e) {
			throw e;
		}
	}

	private JobDetail buildJob(QuartzTaskInformationDTO quartzTaskInformationDTO) {
		JobDetail jobDetail = JobBuilder.newJob(QuartzMainJobFactory.class)
				.withDescription(quartzTaskInformationDTO.getExecuteParamter())
				.withIdentity(quartzTaskInformationDTO.getTaskId(), Scheduler.DEFAULT_GROUP).build();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		jobDataMap.put("id", quartzTaskInformationDTO.getTaskId());
		jobDataMap.put("name", quartzTaskInformationDTO.getTaskName());
		jobDataMap.put("sendType", quartzTaskInformationDTO.getSendType().toString());
		jobDataMap.put("url", quartzTaskInformationDTO.getUrl());
		jobDataMap.put("executeParameter", quartzTaskInformationDTO.getExecuteParamter());
		return jobDetail;
	}

	private Trigger selectTrigger(TriggerKey triggerKey, QuartzTaskInformationDTO info) {
		Trigger trigger;
		if (info.getTriggerType() == TriggerType.CORN) {
			trigger = setCronTrigger(triggerKey, info);
		} else if (info.getTriggerType() == TriggerType.Simple) {
			trigger = setSimpleTrigger(triggerKey, info);
		} else {
			trigger = setCronTrigger(triggerKey, info);
		}
		return trigger;
	}

	private CronTrigger setCronTrigger(TriggerKey triggerKey, QuartzTaskInformationDTO info) {
		int triggerPriority = info.getTriggerPriority();
		triggerPriority = triggerPriority == 0 ? 5 : triggerPriority;
		CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(info.getCornExp());
		this.setCronMisFireType(info.getCronMisfire(), schedBuilder);
		return TriggerBuilder.newTrigger().withIdentity(triggerKey).withPriority(triggerPriority)
				.withSchedule(schedBuilder).build();
	}

	private SimpleTrigger setSimpleTrigger(TriggerKey triggerKey, QuartzTaskInformationDTO info) {
		SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInSeconds(info.getIntervalInSeconds()).withRepeatCount(info.getRepeatCount());
		this.setSimpleMisFireType(info.getSimpleMisfire(), simpleScheduleBuilder);
		return TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(simpleScheduleBuilder).build();
	}

	private void setCronMisFireType(CronMisfire corn, CronScheduleBuilder cronScheduleBuilder) {
		switch (String.valueOf(corn)) {
		case "DO_NOTHING":
			cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
			break;
		case "FIRE_ONCE_NOW":
			cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
			break;
		case "IGNORE_MISFIRES":
			cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
			break;
		default:
			cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
			break;
		}
	}

	private void setSimpleMisFireType(SimpleMisfire simple, SimpleScheduleBuilder simpleScheduleBuilder) {
		switch (String.valueOf(simple)) {
		case "FIRE_NOW":
			simpleScheduleBuilder.withMisfireHandlingInstructionFireNow();
			break;
		case "IGNORE_MISFIRES":
			simpleScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
			break;
		case "NEXT_WITH_EXISTING_COUNT":
			simpleScheduleBuilder.withMisfireHandlingInstructionNextWithExistingCount();
			break;
		case "NEXT_WITH_REMAINING_COUNT":
			simpleScheduleBuilder.withMisfireHandlingInstructionNextWithRemainingCount();

			break;
		case "NOW_WITH_EXISTING_COUNT":
			simpleScheduleBuilder.withMisfireHandlingInstructionNextWithRemainingCount();
			break;
		case "NOW_WITH_REMAINING_COUNT":
			simpleScheduleBuilder.withMisfireHandlingInstructionNowWithRemainingCount();
			break;
		default:
			simpleScheduleBuilder.withMisfireHandlingInstructionFireNow();
			break;
		}

	}

	@Override
	public boolean UnscheduleJob(String jobId) throws CustomSchedulerServiceException {
		try {
			this.delete(jobId, this.scheduler.DEFAULT_GROUP);
		} catch (SchedulerException e) {
			throw new CustomSchedulerServiceException(e.getMessage(), e, true, true);
		}
		return true;

	}

	private void delete(String jobId, String jobGroup) throws SchedulerException {
		// boolean flag = false;
		TriggerKey triggerKey = TriggerKey.triggerKey(jobId, jobGroup);
		if (checkExists(triggerKey)) {
			scheduler.pauseTrigger(triggerKey);
			scheduler.unscheduleJob(triggerKey);
			return;
		}
		throw new CustomSchedulerServiceException("job not exisits");
	}

	private void resume(String jobId, String jobGroup) throws SchedulerException {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobId, jobGroup);
		if (checkExists(triggerKey)) {
			scheduler.resumeTrigger(triggerKey);
			return;
		}
		throw new CustomSchedulerServiceException("job not exisits");
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean resumeJob(String jobId) throws CustomSchedulerServiceException {
		try {
			this.resume(jobId, this.scheduler.DEFAULT_GROUP);
		} catch (SchedulerException e) {
			throw new CustomSchedulerServiceException(e.getMessage(), e, true, true);
		}
		return true;
	}

	@SuppressWarnings("static-access")
	public boolean pausejob(String jobId) throws CustomSchedulerServiceException {
		try {
			this.pause(jobId, this.scheduler.DEFAULT_GROUP);
		} catch (SchedulerException e) {
			throw new CustomSchedulerServiceException(e.getMessage(), e, true, true);
		}
		return true;
	}

	private void pause(String jobName, String jobGroup) throws SchedulerException {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		if (checkExists(triggerKey)) {
			scheduler.pauseTrigger(triggerKey);
			String schedulerName = scheduler.getSchedulerName();
			log.info("schedulerName:{},jobName:{},jobGroup:{} freez", schedulerName, jobName, jobGroup);
			return;
		}
		throw new CustomSchedulerServiceException("job not exisits");
	}

	private boolean checkExists(TriggerKey triggerKey) throws SchedulerException {
		return scheduler.checkExists(triggerKey);
	}

}
