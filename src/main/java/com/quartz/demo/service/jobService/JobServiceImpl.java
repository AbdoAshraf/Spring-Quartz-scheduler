package com.quartz.demo.service.jobService;

import java.time.LocalDateTime;
import java.util.UUID;

import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.quartz.demo.dto.QuartzTaskEvent;
import com.quartz.demo.service.info.QuartzInformationService;
import com.quartz.demo.service.messaging.GreetingsService;
import com.quartz.demo.stream.Greetings;
import com.quartz.demo.util.enums.EventType;
import com.quartz.demo.util.enums.SendType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobServiceImpl implements JobService {
	@Autowired
	private GreetingsService greetingsService;
	@Autowired
	private QuartzInformationService quartzInformationService;

	@Override
	public void call(JobDataMap jobDataMap) {
		String sendType = jobDataMap.getString("sendType");

		log.info("Executing Now:taskName={},sendType={},url={}", jobDataMap.getString("name"),
				jobDataMap.getString("sendType"));
		if (sendType.equals(SendType.URL.toString()))
			callUrl(jobDataMap);

		else if (sendType.equals(SendType.KAFKA.toString()))
			callKafka(jobDataMap);
	}

	private void callUrl(JobDataMap jobDataMap) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.getForEntity(jobDataMap.getString("url"), String.class);
			log.info("id={},taskName={},sendtype={}url", jobDataMap.getString("id"), jobDataMap.getString("name"),
					jobDataMap.getString("url"));
			if (response.getStatusCode() != HttpStatus.OK) {
				log.error("id={},taskName={},message={}", jobDataMap.getString("id"), jobDataMap.getString("name"),
						response);
			}
		} catch (Exception ex) {

		}

	}

	private void callKafka(JobDataMap jobDataMap) {
		String eventId = UUID.randomUUID().toString();
		try {
			String message = new StringBuffer(jobDataMap.getString("name")).append(":")
					.append(jobDataMap.getString("id")).append(":").append("eventId:").append(eventId).toString();
			sendMessage(message);
			recordEvent(jobDataMap.getString("id"), eventId, EventType.Success, "streaming done successfully");
			log.info("id={},taskName={},sendtype={}url", jobDataMap.getString("id"), jobDataMap.getString("name"),
					jobDataMap.getString("sendType"));
		} catch (Exception ex) {
			log.error("id={},taskName={},message={}", jobDataMap.getString("id"), jobDataMap.getString("name"),
					ex.getMessage());
			recordEvent(jobDataMap.getString("id"), eventId, EventType.Success, ex.getMessage());
		}
	}

	private void sendMessage(String message) {
		Greetings greetings = Greetings.builder().message(message).timestamp(System.currentTimeMillis()).build();
		greetingsService.sendGreeting(greetings);
	}

	private void recordEvent(String jobId, String eventId, EventType eventType, String message) {
		QuartzTaskEvent quartzTaskEvent = new QuartzTaskEvent(eventId, eventType, LocalDateTime.now(), message);
		quartzInformationService.recordError(quartzTaskEvent, jobId);
	}

}
