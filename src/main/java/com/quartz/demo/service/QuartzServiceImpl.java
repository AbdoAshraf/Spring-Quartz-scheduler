//package com.quartz.demo.service;
//
//import java.util.UUID;
//
//import org.quartz.CronScheduleBuilder;
//import org.quartz.JobBuilder;
//import org.quartz.JobDataMap;
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.quartz.Trigger;
//import org.quartz.TriggerBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.quartz.demo.dto.QuartzTaskInformations;
//import com.quartz.demo.job.DummyJob;
//import com.quartz.demo.payload.ScheduleJobRequest;
//import com.quartz.demo.payload.ScheduleJobResponse;
//
//public class QuartzServiceImpl implements QuartzService {
//
//	@Autowired
//	private Scheduler scheduler;
//
////	@Override
////	public QuartzTaskInformations addJob(QuartzTaskInformations quartzTaskInformations) throws SchedulerException {
//////		JobDetail jobDetail = buildJobDetail(schedulejobRequest);
//////		Trigger trigger = buildJobTrigger(jobDetail);
//////		scheduler.scheduleJob(jobDetail, trigger);
//////		ScheduleJobResponse scheduleEmailResponse = new ScheduleJobResponse(true, jobDetail.getKey().getName(),
//////				jobDetail.getKey().getGroup(), "Email Scheduled Successfully!");
//////		return scheduleEmailResponse;
////		return null;
////	}
//
////	private JobDetail buildJobDetail(ScheduleJobRequest schedulejobRequest) {
////		JobDataMap jobDataMap = new JobDataMap();
////
////		jobDataMap.put("Description", schedulejobRequest.getJobDescription());
////		jobDataMap.put("name", schedulejobRequest.getJobName());
////		// jobDataMap.put("body", scheduleEmailRequest.getBody());
////
////		return JobBuilder.newJob(DummyJob.class).withIdentity(UUID.randomUUID().toString(), "dummy-jobs")
////				.withDescription("Send Email Job").usingJobData(jobDataMap).storeDurably().build();
////	}
////
////	private Trigger buildJobTrigger(JobDetail jobDetail) {
////		return TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity(jobDetail.getKey().getName(), "jobtriggers")
////				.withDescription("Send Email Trigger").withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * ? * * *"))
////				.build();
////	}
//
//	@Override
//	public QuartzTaskInformations addJob(QuartzTaskInformations schedulejobRequest) throws SchedulerException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
