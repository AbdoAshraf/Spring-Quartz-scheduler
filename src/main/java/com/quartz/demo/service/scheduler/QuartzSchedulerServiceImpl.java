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
import com.quartz.demo.job.DummyJob;
import com.quartz.demo.service.QuartzService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuartzSchedulerServiceImpl implements QuartzSchedulerService {

	private Scheduler scheduler;

	private QuartzService quartzService;

	@Autowired
	public QuartzSchedulerServiceImpl(Scheduler scheduler, QuartzService quartzService) {
		this.scheduler = scheduler;

		this.quartzService = quartzService;
	}

	@Override

	public boolean scheduleJob(QuartzTaskInformation quartzTaskInformation) {
		TriggerKey triggerKey = TriggerKey.triggerKey(quartzTaskInformation.getTaskId(), Scheduler.DEFAULT_GROUP);
		JobDetail jobDetail = JobBuilder.newJob(DummyJob.class)
				.withDescription(quartzTaskInformation.getExecuteParamter())
				.withIdentity(quartzTaskInformation.getTaskId(), Scheduler.DEFAULT_GROUP).build();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		jobDataMap.put("id", quartzTaskInformation.getTaskId());
		jobDataMap.put("sendType", quartzTaskInformation.getSendType());
		jobDataMap.put("url", quartzTaskInformation.getUrl());
		jobDataMap.put("executeParameter", quartzTaskInformation.getExecuteParamter());
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartzTaskInformation.getCornExp());
		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withDescription(quartzTaskInformation.getTaskName())
				.withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
		try {
			scheduler.scheduleJob(jobDetail, cronTrigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("taskName={},scheduleRule={} load to quartz success!", quartzTaskInformation.getTaskName());
		return true;
	}

	@Override
	public boolean UnscheduleJob(QuartzTaskInformation quartzTaskInformation) {
		this.delete(quartzTaskInformation.getTaskId(), Scheduler.DEFAULT_GROUP);
		return true;
	}

	private boolean delete(String jobName, String jobGroup) {
		boolean flag = false;
		try {
			if (checkExists(jobName, jobGroup)) {
				TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
				scheduler.pauseTrigger(triggerKey);
				scheduler.unscheduleJob(triggerKey);
				flag = true;
				String schedulerName = scheduler.getSchedulerName();
				log.info("schedulerName:{},jobName:{},jobGroup:{} 删除成功", schedulerName, jobName, jobGroup);
			}
		} catch (SchedulerException e) {
			// throw new ServiceException(e.getMessage());
		}
		return flag;
	}

	/**
	 * 重新开始任务
	 *
	 * @param jobName  任务名称
	 * @param jobGroup 任务分组
	 * @return true:成功 false:失败
	 */
	private boolean resume(String jobName, String jobGroup) {
		boolean flag = false;
		try {
			if (checkExists(jobName, jobGroup)) {
				TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
				scheduler.resumeTrigger(triggerKey);
				String schedulerName = scheduler.getSchedulerName();
				flag = true;
				log.info("schedulerName:{},jobName:{},jobGroup:{},重启成功", schedulerName, jobName, jobGroup);
			}
		} catch (SchedulerException e) {
			log.error(e.getMessage());
		}
		return flag;
	}

	private boolean checkExists(String jobName, String jobGroup) throws SchedulerException {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		return scheduler.checkExists(triggerKey);
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean pausejob(QuartzTaskInformation quartzTaskInformation) {
		try {
			this.pause(quartzTaskInformation.getTaskId(), this.scheduler.DEFAULT_GROUP);

//			quartzTaskInformation.setFrozenTime(LocalDateTime.now());
//			quartzTaskInformation.setJobStatus(JobStatus.FROZEN);
		} catch (Exception e) {
			// throw new ServiceException(e.getMessage());
			//this.recordError(e, quartzTaskInformation.getTaskId());
		}
		this.quartzService.updateJob(quartzTaskInformation);
		return true;
	}

	@Override
	public boolean resumeJob(QuartzTaskInformation quartzTaskInformation) {
		try {
			this.resume(quartzTaskInformation.getTaskId(), this.scheduler.DEFAULT_GROUP);
		} catch (Exception e) {
		}
		this.quartzService.updateJob(quartzTaskInformation);
		return true;
	}

	private boolean pause(String jobName, String jobGroup) {
		boolean flag = false;
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			if (checkExists(jobName, jobGroup)) {
				scheduler.pauseTrigger(triggerKey);
				flag = true;
				String schedulerName = scheduler.getSchedulerName();
				log.info("schedulerName:{},jobName:{},jobGroup:{} 暂停成功", schedulerName, jobName, jobGroup);
			}
		} catch (SchedulerException e) {
		}
		return flag;
	}

}
