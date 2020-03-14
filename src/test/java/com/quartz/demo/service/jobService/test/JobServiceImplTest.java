package com.quartz.demo.service.jobService.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.JobDataMap;
import org.springframework.web.client.RestTemplate;

import com.quartz.demo.service.info.QuartzInformationService;
import com.quartz.demo.service.jobService.JobServiceImpl;
import com.quartz.demo.service.messaging.GreetingsService;

class JobServiceImplTest {
	@InjectMocks
	JobServiceImpl jobServiceImpl;
	@Mock
	GreetingsService greetingsService;
	@Mock
	RestTemplate restTemplate;

	@Mock
	QuartzInformationService quartzInformationService;

	JobDataMap jobDataMap;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		jobDataMap = new JobDataMap();
		jobDataMap.put("id", "id");
		jobDataMap.put("name", "dymyTask");
		jobDataMap.put("sendType", "URL");
		jobDataMap.put("url", "url");
		jobDataMap.put("executeParameter", "placeHolder");
	}

	@Test
	void testCall_URL() {
		jobServiceImpl.call(jobDataMap);
		Mockito.verify(restTemplate).getForEntity(jobDataMap.getString("url"), String.class);
	}

	@Test
	void testCall_Kafa() {
		jobDataMap.put("sendType", "KAFKA");
		// Greetings greetings = Mockito.spy(new Greetings());
		jobServiceImpl.call(jobDataMap);
		Mockito.verify(greetingsService).sendGreeting(Mockito.any());

	}

}
