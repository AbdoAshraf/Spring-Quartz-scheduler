package com.quartz.demo.service.info.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import com.quartz.demo.dto.QartzTaskAnalyticsDTO;
import com.quartz.demo.dto.QuartzTaskConfigDTO;
import com.quartz.demo.dto.QuartzTaskEventDTO;
import com.quartz.demo.dto.QuartzTaskInformationDTO;
import com.quartz.demo.exception.InfoServiceExcseption;
import com.quartz.demo.io.entity.QartzTaskAnalyticsEntity;
import com.quartz.demo.io.entity.QuartzTaskConfigEntity;
import com.quartz.demo.io.entity.QuartzTaskEventEntity;
import com.quartz.demo.io.entity.QuartzTaskInformationEntity;
import com.quartz.demo.io.repo.QuartzTaskInformationRepo;
import com.quartz.demo.service.info.QuartzInformationServiceImpl;
import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.JobStatus;
import com.quartz.demo.util.enums.SendType;
import com.quartz.demo.util.enums.SimpleMisfire;
import com.quartz.demo.util.enums.TriggerType;

class QuartzInformationServiceImplTest {

	@InjectMocks
	QuartzInformationServiceImpl quartzInformationService;

	@Mock
	QuartzTaskInformationRepo quartzTaskInformationRepo;

	@Spy
	ModelMapper modelMapper;

	QuartzTaskInformationEntity quartzTaskInformationEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		quartzTaskInformationEntity = new QuartzTaskInformationEntity();
		quartzTaskInformationEntity.setId(1L);
		quartzTaskInformationEntity.setTaskId("any");
		quartzTaskInformationEntity.setTaskName("dymmyTask");
		quartzTaskInformationEntity.setQuartzTaskConfig(new QuartzTaskConfigEntity(1L, SendType.KAFKA, "url", "corn",
				"executeParamter", TriggerType.CORN, 3, 1, SimpleMisfire.FIRE_NOW, CronMisfire.DO_NOTHING, 0));
		quartzTaskInformationEntity.setQuartzTaskEventsList(new LinkedList<QuartzTaskEventEntity>());
		quartzTaskInformationEntity.setQartzTaskAnalytics(new QartzTaskAnalyticsEntity(1L, JobStatus.FROZEN, 0));
	}

	@Test
	final void testGetJobDetails() {

		when(quartzTaskInformationRepo.findByTaskId(org.mockito.ArgumentMatchers.anyString()))
				.thenReturn(quartzTaskInformationEntity);

		QuartzTaskInformationDTO quartzTaskInformationTest = quartzInformationService.getJobDetails("any");

		assertNotNull(quartzTaskInformationTest);
		assertEquals("dymmyTask", quartzTaskInformationTest.getTaskName());

	}

	@Test
	final void testGetJobDetails_InfoServiceExcseptionException() {
		// quartzTaskInformationEntity = null;
		when(quartzTaskInformationRepo.findByTaskId(org.mockito.ArgumentMatchers.anyString())).thenReturn(null);

		assertThrows(InfoServiceExcseption.class,

				() -> {
					quartzInformationService.getJobDetails("test");
				}

		);
	}

	@Test
	final void testInsetNewTask() {
		QuartzTaskInformationDTO quartzTaskInformationDTO = new QuartzTaskInformationDTO();
		// quartzTaskInformationDTO.setTaskId("ay7aga");
		quartzTaskInformationDTO.setTaskName("dymy");
		quartzTaskInformationDTO.setQuartzTaskConfig(new QuartzTaskConfigDTO(SendType.KAFKA, "url", "corn",
				"executeParamter", TriggerType.CORN, 3, 0, SimpleMisfire.FIRE_NOW, CronMisfire.DO_NOTHING, 0));
		quartzTaskInformationDTO.setQartzTaskAnalytics(new QartzTaskAnalyticsDTO());
		quartzTaskInformationDTO.setQuartzTaskEventsList(new ArrayList<>());
		when(quartzTaskInformationRepo.save(org.mockito.ArgumentMatchers.any(QuartzTaskInformationEntity.class)))
				.thenReturn(quartzTaskInformationEntity);

		QuartzTaskInformationDTO storedQuartzTaskInformation = quartzInformationService
				.insertNewJob(quartzTaskInformationDTO);
		assertNotNull(storedQuartzTaskInformation);
		assertEquals(storedQuartzTaskInformation.getSendType(), SendType.KAFKA);
		assertEquals(storedQuartzTaskInformation.getCronMisfire(), CronMisfire.DO_NOTHING);
	}

	@Test
	final void updateJobStatus_InfoServiceExcseptionExceptionInvalidTaskID() {
		// when(quartzTaskInformationRepo.findByTaskId(org.mockito.ArgumentMatchers.anyString())).thenReturn(null);

		Exception exception = assertThrows(InfoServiceExcseption.class,

				() -> {
					quartzInformationService.updateJobStatus(null, JobStatus.FROZEN);
				}

		);
		assertTrue(exception.getMessage().contains("invalid task id"));

	}

	@Test
	final void updateJobStatus_InfoServiceExcseptionExceptionInvalidJobStatus() {
		when(quartzTaskInformationRepo.findByTaskId(org.mockito.ArgumentMatchers.anyString()))
				.thenReturn(quartzTaskInformationEntity);

		Exception exception = assertThrows(InfoServiceExcseption.class,

				() -> {
					quartzInformationService.updateJobStatus("id", null);
				}

		);
		assertTrue(exception.getMessage().contains("invalid jobStatus"));

	}

//	new QuartzTaskEventDTO("id",EventType.Error
//			,LocalDateTime.now(),"message")
	@Test
	final void updateJobStatus() {
		when(quartzTaskInformationRepo.findByTaskId(org.mockito.ArgumentMatchers.anyString()))
				.thenReturn(quartzTaskInformationEntity);
		quartzInformationService.updateJobStatus("id", JobStatus.FROZEN);
		Mockito.verify(quartzTaskInformationRepo).findByTaskId("id");
	}

	@Test
	final void testRecordError_InfoServiceExcseptionExceptionIvalidTaskId() {
		when(quartzTaskInformationRepo.findByTaskId(org.mockito.ArgumentMatchers.anyString())).thenReturn(null);

		Exception exception = assertThrows(InfoServiceExcseption.class,

				() -> {
					quartzInformationService.recordEvent(new QuartzTaskEventDTO(), null);
				}

		);
		assertTrue(exception.getMessage().contains("invalid task id"));
	}

	@Test
	final void testRecordError() {
		when(quartzTaskInformationRepo.findByTaskId(org.mockito.ArgumentMatchers.anyString()))
				.thenReturn(quartzTaskInformationEntity);
		quartzInformationService.recordEvent(new QuartzTaskEventDTO(), "id");
		Mockito.verify(quartzTaskInformationRepo).findByTaskId("id");
	}

}
