package com.quartz.demo.job;

import java.time.LocalDateTime;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.quartz.demo.config.ApplicationContextHolder;
import com.quartz.demo.dto.Greetings;
import com.quartz.demo.dto.QuartzTaskError;
import com.quartz.demo.service.QuartzService;
import com.quartz.demo.service.QuartzServiceImpl;
import com.quartz.demo.service.kafka.GreetingsService;
import com.quartz.demo.util.enums.SendType;

import lombok.extern.slf4j.Slf4j;

@DisallowConcurrentExecution
@Slf4j
public class QuartzMainJobFactory implements Job {

	@Autowired
	private GreetingsService greetingsService;
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
		String id = jobDataMap.getString("id");
		String taskName = jobDataMap.getString("name");
		// String executorNo = jobDataMap.getString("executorNo");
		String sendType = jobDataMap.getString("sendType");
		String url = jobDataMap.getString("url");
		String executeParameter = jobDataMap.getString("executeParameter");
		log.info("Executing Now:taskName={},sendType={},url={},executeParameter={}", taskName, sendType,
				executeParameter);
		QuartzService quartzService = (QuartzServiceImpl) ApplicationContextHolder.getBean("quartzServiceImpl");
		// QuartzTaskInformation records = quartzService.getJobDetails(id)

		QuartzTaskError quartzTaskError = new QuartzTaskError();
		if (sendType.equals(SendType.URL.toString())) {
			try {
				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
				log.info("id={},taskName={},sendtype={}url", id, taskName, sendType);
				if (response.getStatusCode() != HttpStatus.OK) {
					quartzTaskError.setExecuteTime(LocalDateTime.now());
					quartzTaskError.setFailReason(response.toString());
					quartzService.recordError(quartzTaskError, id);
					log.error("id={},taskName={},message={}", id, taskName, response);
				}
			} catch (Exception ex) {
				quartzTaskError.setExecuteTime(LocalDateTime.now());
				quartzTaskError.setFailReason(ex.getMessage());
				quartzService.recordError(quartzTaskError, id);
				log.error("id={},taskName={},message={}", id, taskName, ex.getMessage());
			}

		} else if (sendType.equals(SendType.KAFKA.toString())) {
			try {
				String message = new StringBuffer(taskName).append(":").append(id).append(":").append(executeParameter)
						.toString();
				this.sendMessage(message);
				log.info("id={},taskName={},sendtype={}url", id, taskName, sendType);
			} catch (Exception ex) {
				quartzTaskError.setExecuteTime(LocalDateTime.now());
				quartzTaskError.setFailReason(ex.getMessage());
				quartzService.recordError(quartzTaskError, id);
				log.error("id={},taskName={},message={}", id, taskName, ex.getMessage());
			}
		}
	}

	public void sendMessage(String message) {

		Greetings greetings = Greetings.builder()
	            .message(message)
	            .timestamp(System.currentTimeMillis())
	            .build();
	        greetingsService.sendGreeting(greetings);
	}

}
