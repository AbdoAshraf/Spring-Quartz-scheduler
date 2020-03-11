package com.quartz.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.quartz.demo.dto.QartzTaskAnalytics;
import com.quartz.demo.dto.QuartzTaskConfig;
import com.quartz.demo.dto.QuartzTaskEvent;
import com.quartz.demo.dto.QuartzTaskInformation;
import com.quartz.demo.exception.InfoServiceExcseption;
import com.quartz.demo.io.entity.QuartzTaskConfigEntity;
import com.quartz.demo.io.entity.QuartzTaskEventEntity;
import com.quartz.demo.io.entity.QuartzTaskInformationEntity;
import com.quartz.demo.io.repo.QuartzTaskInformationRepo;
import com.quartz.demo.service.info.QuartzInformationServiceImpl;
import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.JobStatus;
import com.quartz.demo.util.enums.SendType;
import com.quartz.demo.util.enums.SimpleMisfire;

class QuartzInformationServiceImplTest {

	@Mock
	QuartzInformationServiceImpl quartzInformationService;

	@Mock
	QuartzTaskInformationRepo quartzTaskInformationRepo;

	QuartzTaskInformationEntity quartzTaskInformationEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		quartzTaskInformationEntity = new QuartzTaskInformationEntity();
		quartzTaskInformationEntity.setId(1L);
		quartzTaskInformationEntity.setTaskId("any");
		quartzTaskInformationEntity.setTaskName("dymmyTask");
		quartzTaskInformationEntity.setQuartzTaskConfig(new QuartzTaskConfigEntity(1L, SendType.KAFKA, null, "corn",
				"executeParamter", "triggerType", 5, SimpleMisfire.FIRE_NOW, CronMisfire.DO_NOTHING, 2));
		quartzTaskInformationEntity.setQuartzTaskErrorsList(new ArrayList<QuartzTaskEventEntity>());
	}

	@Test
	final void testGetJobDetails() {

		when(quartzTaskInformationRepo.findByTaskId(org.mockito.ArgumentMatchers.anyString()))
				.thenReturn(Optional.ofNullable(quartzTaskInformationEntity));

		QuartzTaskInformation quartzTaskInformationTest = quartzInformationService.getJobDetails("any");

		assertNotNull(quartzTaskInformationTest);
		assertEquals("dymmyTask", quartzTaskInformationTest.getTaskName());

	}

	@Test
	final void testGetJobDetails_InfoServiceExcseptionException() {
		quartzTaskInformationEntity = null;
		when(quartzTaskInformationRepo.findByTaskId(org.mockito.ArgumentMatchers.anyString()))
				.thenReturn(Optional.ofNullable(quartzTaskInformationEntity));

		assertThrows(InfoServiceExcseption.class,

				() -> {
					quartzInformationService.getJobDetails("test");
				}

		);
	}

	@Test
	final void testInsetNewTask() {
		QuartzTaskInformation quartzTaskInformation = new QuartzTaskInformation();
		quartzTaskInformation.setTaskId("ay7aga");
		quartzTaskInformation.setTaskName("dymy");
		quartzTaskInformation.setQuartzTaskConfig(new QuartzTaskConfig(SendType.KAFKA, null, "corn", "executeParamter",
				"triggerType", 5, 2, SimpleMisfire.FIRE_NOW, CronMisfire.DO_NOTHING, 2));
		quartzTaskInformation
				.setQartzTaskAnalytics(new QartzTaskAnalytics(JobStatus.FROZEN, LocalDateTime.now(), null, null, 0, 0));
		quartzTaskInformation.setQuartzTaskErrorsList(new LinkedList<QuartzTaskEvent>());
		when(quartzTaskInformationRepo.save(org.mockito.ArgumentMatchers.any(QuartzTaskInformationEntity.class)))
				.thenReturn(quartzTaskInformationEntity);

		QuartzTaskInformation storedQuartzTaskInformation = quartzInformationService
				.insertNewJob(quartzTaskInformation);
		assertNotNull(storedQuartzTaskInformation);
		assertEquals(storedQuartzTaskInformation.getTaskId(), "ay7aga");
		assertEquals(storedQuartzTaskInformation.getSendType(), SendType.KAFKA);
	}

}
