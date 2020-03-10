package com.quartz.demo.service.scheduler;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quartz.demo.dto.QuartzTaskInformation;
import com.quartz.demo.exception.CustomSchedulerServiceException;
import com.quartz.demo.job.QuartzMainJobFactory;
import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.SimpleMisfire;

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
		String jobName = quartzTaskInformation.getTaskName();// info.getJobName();
		String jobGroup = Scheduler.DEFAULT_GROUP;
		JobDetail jobDetail = JobBuilder.newJob(QuartzMainJobFactory.class)
				.withDescription(quartzTaskInformation.getExecuteParamter())
				.withIdentity(quartzTaskInformation.getTaskId(), Scheduler.DEFAULT_GROUP).build();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		jobDataMap.put("id", quartzTaskInformation.getTaskId());
		jobDataMap.put("name", quartzTaskInformation.getTaskName());
		jobDataMap.put("sendType", quartzTaskInformation.getSendType().toString());
		jobDataMap.put("url", quartzTaskInformation.getUrl());
		jobDataMap.put("executeParameter", quartzTaskInformation.getExecuteParamter());
		if (checkExists(jobName, jobGroup)) {
			throw new CustomSchedulerServiceException(String.format("Job already active", jobName, jobGroup));
		}
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		Trigger trigger = this.selectTrigger(triggerKey, quartzTaskInformation);
		scheduler.scheduleJob(jobDetail, trigger);
		String schedulerName = scheduler.getSchedulerName();
		log.info("schedulerName:{},jobName:{},jobGroup:{},jobClass:{}", schedulerName, jobName, jobGroup);
	}

	private Trigger selectTrigger(TriggerKey triggerKey, QuartzTaskInformation info) {
		return TriggerType.valueOf(info.getTriggerType().toUpperCase()).readTrigger(triggerKey, info);
	}

	public enum TriggerType {
		CRON {
			@Override
			Trigger readTrigger(TriggerKey triggerKey, QuartzTaskInformation info) {
				int triggerPriority = info.getTriggerPriority();
				triggerPriority = triggerPriority == 0 ? 5 : triggerPriority;
				CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(info.getCornExp());
				this.setCronMisFireType(info, schedBuilder);
				return TriggerBuilder.newTrigger().withIdentity(triggerKey).withPriority(triggerPriority)
						.withSchedule(schedBuilder).build();
			}
		},
		SIMPLE {
			@Override
			Trigger readTrigger(TriggerKey triggerKey, QuartzTaskInformation info) {
				SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(info.getIntervalInSeconds()).withRepeatCount(info.getRepeatCount());
				this.setSimpleMisFireType(info, simpleScheduleBuilder);
				return TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(simpleScheduleBuilder).build();
			}
		};

		abstract Trigger readTrigger(TriggerKey triggerKey, QuartzTaskInformation info);
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

	private boolean delete(String jobName, String jobGroup) throws SchedulerException {
		boolean flag = false;
		if (checkExists(jobName, jobGroup)) {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			scheduler.pauseTrigger(triggerKey);
			scheduler.unscheduleJob(triggerKey);
			flag = true;
		}
		return flag;
	}

	private boolean resume(String jobName, String jobGroup) throws SchedulerException {
		boolean flag = false;
		if (checkExists(jobName, jobGroup)) {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			scheduler.resumeTrigger(triggerKey);
			// String schedulerName = scheduler.getSchedulerName();
			flag = true;
		}
		return flag;
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

	private boolean pause(String jobName, String jobGroup) throws SchedulerException {
		boolean flag = false;
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		if (checkExists(jobName, jobGroup)) {
			scheduler.pauseTrigger(triggerKey);
			flag = true;
			String schedulerName = scheduler.getSchedulerName();
			log.info("schedulerName:{},jobName:{},jobGroup:{} 暂停成功", schedulerName, jobName, jobGroup);
		}
		return flag;
	}

	private boolean checkExists(String jobName, String jobGroup) throws SchedulerException {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		return scheduler.checkExists(triggerKey);
	}

}
