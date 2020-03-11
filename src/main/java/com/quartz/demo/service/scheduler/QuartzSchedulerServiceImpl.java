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

import com.quartz.demo.dto.QuartzTaskInformation;
import com.quartz.demo.exception.CustomSchedulerServiceException;
import com.quartz.demo.jobfactory.QuartzMainJobFactory;
import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.SimpleMisfire;
import com.quartz.demo.util.enums.TriggerType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuartzSchedulerServiceImpl implements QuartzSchedulerService {

	private Scheduler scheduler;

	@Autowired
	public QuartzSchedulerServiceImpl(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public boolean scheduleJob(QuartzTaskInformation quartzTaskInformation) throws SchedulerException {
		this.addJob(quartzTaskInformation);
		return true;
	}

	private void addJob(QuartzTaskInformation quartzTaskInformation) throws SchedulerException {
		String jobGroup = Scheduler.DEFAULT_GROUP;
		TriggerKey triggerKey = TriggerKey.triggerKey(quartzTaskInformation.getTaskId(), jobGroup);
		if (checkExists(triggerKey)) {
			throw new CustomSchedulerServiceException(
					String.format("Job already active", quartzTaskInformation.getTaskName(), jobGroup));
		}
		JobDetail jobDetail = buildJob(quartzTaskInformation);
		try {
			Trigger trigger = this.selectTrigger(triggerKey, quartzTaskInformation);
			scheduler.scheduleJob(jobDetail, trigger);
			String schedulerName = scheduler.getSchedulerName();
			log.info("schedulerName:{},jobName:{},jobGroup:{},jobClass:{}", schedulerName,
					quartzTaskInformation.getTaskName(), jobGroup);
		} catch (SchedulerException e) {
			throw e;
		}
	}

	private JobDetail buildJob(QuartzTaskInformation quartzTaskInformation) {
		JobDetail jobDetail = JobBuilder.newJob(QuartzMainJobFactory.class)
				.withDescription(quartzTaskInformation.getExecuteParamter())
				.withIdentity(quartzTaskInformation.getTaskId(), Scheduler.DEFAULT_GROUP).build();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		jobDataMap.put("id", quartzTaskInformation.getTaskId());
		jobDataMap.put("name", quartzTaskInformation.getTaskName());
		jobDataMap.put("sendType", quartzTaskInformation.getSendType().toString());
		jobDataMap.put("url", quartzTaskInformation.getUrl());
		jobDataMap.put("executeParameter", quartzTaskInformation.getExecuteParamter());
		return jobDetail;
	}

	private Trigger selectTrigger(TriggerKey triggerKey, QuartzTaskInformation info) {
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

	private CronTrigger setCronTrigger(TriggerKey triggerKey, QuartzTaskInformation info) {
		int triggerPriority = info.getTriggerPriority();
		triggerPriority = triggerPriority == 0 ? 5 : triggerPriority;
		CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(info.getCornExp());
		this.setCronMisFireType(info, schedBuilder);
		return TriggerBuilder.newTrigger().withIdentity(triggerKey).withPriority(triggerPriority)
				.withSchedule(schedBuilder).build();
	}

	private SimpleTrigger setSimpleTrigger(TriggerKey triggerKey, QuartzTaskInformation info) {
		SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInSeconds(info.getIntervalInSeconds()).withRepeatCount(info.getRepeatCount());
		this.setSimpleMisFireType(info, simpleScheduleBuilder);
		return TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(simpleScheduleBuilder).build();
	}

	private void setCronMisFireType(QuartzTaskInformation info, CronScheduleBuilder cronScheduleBuilder) {
		if (CronMisfire.DO_NOTHING == info.getCronMisfire())
			cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
		else if (CronMisfire.FIRE_ONCE_NOW == info.getCronMisfire())
			cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
		else if (CronMisfire.IGNORE_MISFIRES == info.getCronMisfire())
			cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
		else
			cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
	}

	private void setSimpleMisFireType(QuartzTaskInformation info, SimpleScheduleBuilder simpleScheduleBuilder) {
		if (SimpleMisfire.FIRE_NOW == info.getSimpleMisfire())
			simpleScheduleBuilder.withMisfireHandlingInstructionFireNow();
		else if (SimpleMisfire.IGNORE_MISFIRES == info.getSimpleMisfire())
			simpleScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
		else if (SimpleMisfire.NEXT_WITH_EXISTING_COUNT == info.getSimpleMisfire())
			simpleScheduleBuilder.withMisfireHandlingInstructionNextWithExistingCount();
		else if (SimpleMisfire.NOW_WITH_EXISTING_COUNT == info.getSimpleMisfire())
			simpleScheduleBuilder.withMisfireHandlingInstructionNowWithExistingCount();
		else if (SimpleMisfire.NEXT_WITH_REMAINING_COUNT == info.getSimpleMisfire())
			simpleScheduleBuilder.withMisfireHandlingInstructionNextWithRemainingCount();
		else if (SimpleMisfire.NOW_WITH_REMAINING_COUNT == info.getSimpleMisfire())
			simpleScheduleBuilder.withMisfireHandlingInstructionNowWithRemainingCount();
		else
			simpleScheduleBuilder.withMisfireHandlingInstructionFireNow();

	}

	@Override
	public boolean UnscheduleJob(QuartzTaskInformation quartzTaskInformation) throws SchedulerException {
		this.delete(quartzTaskInformation.getTaskId(), Scheduler.DEFAULT_GROUP);
		return true;
	}

	private boolean delete(String jobId, String jobGroup) throws SchedulerException {
		boolean flag = false;
		TriggerKey triggerKey = TriggerKey.triggerKey(jobId, jobGroup);
		if (checkExists(triggerKey)) {
			scheduler.pauseTrigger(triggerKey);
			scheduler.unscheduleJob(triggerKey);
			flag = true;
		}
		return flag;
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
	public boolean resumeJob(QuartzTaskInformation quartzTaskInformation) throws SchedulerException {
		this.resume(quartzTaskInformation.getTaskId(), this.scheduler.DEFAULT_GROUP);
		return true;
	}

	@SuppressWarnings("static-access")
	public boolean pausejob(QuartzTaskInformation quartzTaskInformation) throws SchedulerException {
		this.pause(quartzTaskInformation.getTaskId(), this.scheduler.DEFAULT_GROUP);
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
