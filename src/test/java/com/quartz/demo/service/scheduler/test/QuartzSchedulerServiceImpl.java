package com.quartz.demo.service.scheduler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

import com.quartz.demo.dto.QartzTaskAnalyticsDTO;
import com.quartz.demo.dto.QuartzTaskConfigDTO;
import com.quartz.demo.dto.QuartzTaskEventDTO;
import com.quartz.demo.dto.QuartzTaskInformationDTO;
import com.quartz.demo.exception.CustomSchedulerServiceException;
import com.quartz.demo.service.scheduler.QuartzSchedulerServiceImpl;
import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.JobStatus;
import com.quartz.demo.util.enums.SendType;
import com.quartz.demo.util.enums.SimpleMisfire;
import com.quartz.demo.util.enums.TriggerType;

class QuartzSchedulerServiceImplTest {
	@InjectMocks
	QuartzSchedulerServiceImpl quartzSchedulerServiceImpl;

	@Mock
	Scheduler scheduler;

//	@Spy
//	QuartzSchedulerService quartzSchedulerServiceSpy;

	QuartzTaskInformationDTO quartzTaskInformationDTO;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		quartzTaskInformationDTO = new QuartzTaskInformationDTO();
		// quartzTaskInformationDTO.setId(1L);
		quartzTaskInformationDTO.setTaskName("dymmyTask");
		quartzTaskInformationDTO.setTaskId("id");
		quartzTaskInformationDTO.setQuartzTaskConfig(new QuartzTaskConfigDTO(SendType.KAFKA, "url", "0/5 * * ? * * *",
				"executeParamter", TriggerType.CORN, 3, 1, SimpleMisfire.FIRE_NOW, CronMisfire.DO_NOTHING, 0));
		quartzTaskInformationDTO.setQuartzTaskEventsList(new LinkedList<QuartzTaskEventDTO>());
		quartzTaskInformationDTO.setQartzTaskAnalytics(new QartzTaskAnalyticsDTO(JobStatus.FROZEN, 0));
	}

	@Test
	void testsetCronMisFireType() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		CronScheduleBuilder cronSchedule = Mockito
				.spy(CronScheduleBuilder.cronSchedule(quartzTaskInformationDTO.getCornExp()));

		Method m = QuartzSchedulerServiceImpl.class.getDeclaredMethod("setCronMisFireType", CronMisfire.class,
				CronScheduleBuilder.class);
		m.setAccessible(true);
		m.invoke(quartzSchedulerServiceImpl, CronMisfire.DO_NOTHING, cronSchedule);
		Mockito.verify(cronSchedule).withMisfireHandlingInstructionDoNothing();
		m.invoke(quartzSchedulerServiceImpl, CronMisfire.FIRE_ONCE_NOW, cronSchedule);
		Mockito.verify(cronSchedule).withMisfireHandlingInstructionFireAndProceed();
		m.invoke(quartzSchedulerServiceImpl, CronMisfire.IGNORE_MISFIRES, cronSchedule);
		Mockito.verify(cronSchedule).withMisfireHandlingInstructionIgnoreMisfires();
	}

//	@Test
//	void testSelectTrigger() throws NoSuchMethodException, SecurityException, IllegalAccessException,
//			IllegalArgumentException, InvocationTargetException {
//		// Trigger selectTrigger(TriggerKey triggerKey, QuartzTaskInformationDTO info);
//		// TriggerKey triggerKey =
//		// TriggerKey.triggerKey(quartzTaskInformationDTO.getTaskId(), jobGroup);
//		TriggerKey triggerKey = Mockito
//				.spy(TriggerKey.triggerKey(quartzTaskInformationDTO.getTaskId(), Scheduler.DEFAULT_GROUP));
//		Method m = QuartzSchedulerServiceImpl.class.getDeclaredMethod("selectTrigger", TriggerKey.class,
//				QuartzTaskInformationDTO.class);
//		m.setAccessible(true);
//		 Trigger trigger= (Trigger) m.invoke(quartzSchedulerServiceImpl, triggerKey,quartzTaskInformationDTO);
//
//		assertEquals(trigger.get);
//	}

	@Test
	void testScheduleJobCornJob() {
		boolean val = quartzSchedulerServiceImpl.scheduleJob(quartzTaskInformationDTO);
		assertEquals(val, true);
	}

	@Test
	void testScheduleJobSimpleJob() {
		quartzTaskInformationDTO.setQuartzTaskConfig(new QuartzTaskConfigDTO(SendType.KAFKA, null, null,
				"executeParamter", TriggerType.Simple, 3, 1, SimpleMisfire.FIRE_NOW, null, 2));
		boolean val = quartzSchedulerServiceImpl.scheduleJob(quartzTaskInformationDTO);
		assertEquals(val, true);
	}

	@Test
	void testPaseJob() throws SchedulerException {
		when(scheduler
				.checkExists(TriggerKey.triggerKey(quartzTaskInformationDTO.getTaskId(), Scheduler.DEFAULT_GROUP)))
						.thenReturn(true);
		boolean val = quartzSchedulerServiceImpl.pausejob("id");
		assertEquals(val, true);
	}

	@Test
	void testPaseJob_CustomSchedulerServiceException() throws SchedulerException {
		when(scheduler
				.checkExists(TriggerKey.triggerKey(quartzTaskInformationDTO.getTaskId(), Scheduler.DEFAULT_GROUP)))
						.thenReturn(true);

		assertThrows(CustomSchedulerServiceException.class,

				() -> {
					quartzSchedulerServiceImpl.pausejob("invalId");
				});

	}

	@Test
	void testResumeJob() throws SchedulerException {
		when(scheduler
				.checkExists(TriggerKey.triggerKey(quartzTaskInformationDTO.getTaskId(), Scheduler.DEFAULT_GROUP)))
						.thenReturn(true);
		boolean val = quartzSchedulerServiceImpl.resumeJob("id");
		assertEquals(val, true);
	}

	@Test
	void testResumeJob_CustomSchedulerServiceException() throws SchedulerException {
		when(scheduler
				.checkExists(TriggerKey.triggerKey(quartzTaskInformationDTO.getTaskId(), Scheduler.DEFAULT_GROUP)))
						.thenReturn(true);

		assertThrows(CustomSchedulerServiceException.class,

				() -> {
					quartzSchedulerServiceImpl.resumeJob("invalId");
				});

	}

	@Test
	void testUnscheduleJobJob() throws SchedulerException {
		when(scheduler
				.checkExists(TriggerKey.triggerKey(quartzTaskInformationDTO.getTaskId(), Scheduler.DEFAULT_GROUP)))
						.thenReturn(true);
		Mockito.doNothing().when(scheduler)
				.pauseTrigger(TriggerKey.triggerKey(quartzTaskInformationDTO.getTaskId(), Scheduler.DEFAULT_GROUP));
		when(scheduler
				.unscheduleJob(TriggerKey.triggerKey(quartzTaskInformationDTO.getTaskId(), Scheduler.DEFAULT_GROUP)))
						.thenReturn(true);
		boolean val = quartzSchedulerServiceImpl.UnscheduleJob("id");
		assertEquals(val, true);
	}

	@Test
	void testUnscheduleJobJob_CustomSchedulerServiceException() throws SchedulerException {
		when(scheduler
				.checkExists(TriggerKey.triggerKey(quartzTaskInformationDTO.getTaskId(), Scheduler.DEFAULT_GROUP)))
						.thenReturn(true);
		Mockito.doNothing().when(scheduler)
				.pauseTrigger(TriggerKey.triggerKey(quartzTaskInformationDTO.getTaskId(), Scheduler.DEFAULT_GROUP));
		when(scheduler
				.unscheduleJob(TriggerKey.triggerKey(quartzTaskInformationDTO.getTaskId(), Scheduler.DEFAULT_GROUP)))
						.thenReturn(true);

		assertThrows(CustomSchedulerServiceException.class,

				() -> {
					quartzSchedulerServiceImpl.UnscheduleJob("invalId");
				});

	}

}
