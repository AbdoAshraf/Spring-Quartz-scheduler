package com.quartz.demo.service.scheduler;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quartz.demo.dto.QuartzTaskInformation;
import com.quartz.demo.exception.CustomSchedulerServiceException;
import com.quartz.demo.job.QuartzMainJobFactory;

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
	public boolean scheduleJob(QuartzTaskInformation quartzTaskInformation)
			throws CustomSchedulerServiceException, SchedulerException {
		TriggerKey triggerKey = TriggerKey.triggerKey(quartzTaskInformation.getTaskId(), Scheduler.DEFAULT_GROUP);
		JobDetail jobDetail = JobBuilder.newJob(QuartzMainJobFactory.class)
				.withDescription(quartzTaskInformation.getExecuteParamter())
				.withIdentity(quartzTaskInformation.getTaskId(), Scheduler.DEFAULT_GROUP).build();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		jobDataMap.put("id", quartzTaskInformation.getTaskId());
		jobDataMap.put("name", quartzTaskInformation.getTaskName());
		jobDataMap.put("sendType", quartzTaskInformation.getSendType().toString());
		jobDataMap.put("url", quartzTaskInformation.getUrl());
		jobDataMap.put("executeParameter", quartzTaskInformation.getExecuteParamter());
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartzTaskInformation.getCornExp());
		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withDescription(quartzTaskInformation.getTaskName())
				.withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
		log.info("taskName={},scheduleRule={} load to quartz success!", quartzTaskInformation.getTaskName());
		return true;
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
			String schedulerName = scheduler.getSchedulerName();
			log.info("schedulerName:{},jobName:{},jobGroup:{} 删除成功", schedulerName, jobName, jobGroup);
		}
		return flag;
	}

	private boolean resume(String jobName, String jobGroup) throws SchedulerException {
		boolean flag = false;
		if (checkExists(jobName, jobGroup)) {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			scheduler.resumeTrigger(triggerKey);
			String schedulerName = scheduler.getSchedulerName();
			flag = true;
			log.info("schedulerName:{},jobName:{},jobGroup:{},重启成功", schedulerName, jobName, jobGroup);
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
