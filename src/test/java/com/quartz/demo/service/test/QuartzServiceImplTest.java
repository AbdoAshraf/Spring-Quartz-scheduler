package com.quartz.demo.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.quartz.demo.dto.QartzTaskAnalyticsDTO;
import com.quartz.demo.dto.QuartzTaskConfigDTO;
import com.quartz.demo.dto.QuartzTaskEventDTO;
import com.quartz.demo.dto.QuartzTaskInformationDTO;
import com.quartz.demo.exception.CustomSchedulerServiceException;
import com.quartz.demo.exception.ValidationException;
import com.quartz.demo.service.QuartzServiceImpl;
import com.quartz.demo.service.info.QuartzInformationService;
import com.quartz.demo.service.scheduler.QuartzSchedulerService;
import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.JobStatus;
import com.quartz.demo.util.enums.SendType;
import com.quartz.demo.util.enums.SimpleMisfire;
import com.quartz.demo.util.enums.TriggerType;

class QuartzServiceImplTest {
	@InjectMocks
	QuartzServiceImpl QuartzServiceImpl;
	@Mock
	QuartzInformationService quartzInformationService;
	@Mock
	QuartzSchedulerService quartzSchedulerService;
	QuartzTaskInformationDTO quartzTaskInformationDTO;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		quartzTaskInformationDTO = new QuartzTaskInformationDTO();
		// quartzTaskInformationDTO.setId(1L);
		quartzTaskInformationDTO.setTaskName("dymmyTask");
		quartzTaskInformationDTO.setTaskId("id");
		quartzTaskInformationDTO.setQuartzTaskConfig(new QuartzTaskConfigDTO(SendType.KAFKA, "url", "corn",
				"executeParamter", TriggerType.CORN, 3, 1, SimpleMisfire.FIRE_NOW, CronMisfire.DO_NOTHING, 0));
		quartzTaskInformationDTO.setQuartzTaskEventsList(new LinkedList<QuartzTaskEventDTO>());
		quartzTaskInformationDTO.setQartzTaskAnalytics(new QartzTaskAnalyticsDTO(JobStatus.FROZEN, 0));
	}

	@Test
	void testInsertNewJob() {
		when(quartzInformationService.insertNewJob(quartzTaskInformationDTO)).thenReturn(quartzTaskInformationDTO);

		QuartzTaskInformationDTO returnedValue = QuartzServiceImpl.insertNewJob(quartzTaskInformationDTO);
		assertNotNull(returnedValue);
		assertEquals(returnedValue.getCronMisfire(), quartzTaskInformationDTO.getCronMisfire());
		// assertEquals(returnedValue.getJobStatus(),
		// quartzTaskInformationDTO.getJobStatus());

	}

	@Test
	final void testInsertNewJob_ValidationException() {
		QuartzTaskConfigDTO quartzTaskConfigDTO = new QuartzTaskConfigDTO(SendType.URL, null, "corn", "executeParamter",
				TriggerType.CORN, 3, 1, SimpleMisfire.FIRE_NOW, CronMisfire.DO_NOTHING, 0);
		quartzTaskInformationDTO.setQuartzTaskConfig(quartzTaskConfigDTO);
		this.assertValidationExceptionUtil(quartzTaskInformationDTO, "messing url");

		quartzTaskConfigDTO = new QuartzTaskConfigDTO(SendType.URL, "url", null, "executeParamter", TriggerType.CORN, 3,
				1, SimpleMisfire.FIRE_NOW, null, 0);
		quartzTaskInformationDTO.setQuartzTaskConfig(quartzTaskConfigDTO);
		this.assertValidationExceptionUtil(quartzTaskInformationDTO, "messing corn exp");

		quartzTaskConfigDTO = new QuartzTaskConfigDTO(SendType.URL, "url", "corn", "executeParamter",
				TriggerType.Simple, 3, 1, SimpleMisfire.FIRE_NOW, null, 0);
		quartzTaskInformationDTO.setQuartzTaskConfig(quartzTaskConfigDTO);

		this.assertValidationExceptionUtil(quartzTaskInformationDTO, "messing time interval");
	}

	private void assertValidationExceptionUtil(QuartzTaskInformationDTO quartzTaskInformationDTO, String message) {

		Exception exception = assertThrows(ValidationException.class,

				() -> {
					QuartzServiceImpl.insertNewJob(quartzTaskInformationDTO);
				});

		assertTrue(exception.getMessage().contains(message));
	}

	@Test
	void testGetJobDetails() {
		when(quartzInformationService.getJobDetails("id")).thenReturn(quartzTaskInformationDTO);
		QuartzTaskInformationDTO returnedValue = QuartzServiceImpl.getJobDetails("id");
		assertNotNull(returnedValue);
		assertEquals(returnedValue.getCronMisfire(), quartzTaskInformationDTO.getCronMisfire());
		// assertEquals(returnedValue.getJobStatus(),
		// quartzTaskInformationDTO.getJobStatus());
	}

	@Test
	void testFreezJob() {
		when(quartzSchedulerService.scheduleJob(quartzTaskInformationDTO)).thenReturn(true);
		when(quartzInformationService.getJobDetails("id")).thenReturn(quartzTaskInformationDTO);
		boolean val = QuartzServiceImpl.freezJob("id");
		Mockito.verify(quartzInformationService).getJobDetails("id");
		Mockito.verify(quartzInformationService).updateJobStatus("id", JobStatus.FROZEN);
		assertEquals(val, true);
	}

	@Test
	void testFreezJob__CustomSchedulerServiceException() {
		when(quartzSchedulerService.pausejob("id")).thenThrow(CustomSchedulerServiceException.class);
		when(quartzInformationService.getJobDetails("id")).thenReturn(quartzTaskInformationDTO);
		assertThrows(CustomSchedulerServiceException.class, () -> {
			QuartzServiceImpl.freezJob("id");
		});
	}

	@Test
	void testResumeJob() {
		when(quartzSchedulerService.resumeJob("id")).thenReturn(true);
		when(quartzInformationService.getJobDetails("id")).thenReturn(quartzTaskInformationDTO);
		boolean val = QuartzServiceImpl.ResumeJob("id");
		Mockito.verify(quartzInformationService).getJobDetails("id");
		Mockito.verify(quartzInformationService).updateJobStatus("id", JobStatus.UNFROZEN);
		assertEquals(val, true);
	}

	@Test
	void testResumeJob_CustomSchedulerServiceException() {
		when(quartzSchedulerService.resumeJob("id")).thenThrow(CustomSchedulerServiceException.class);
		when(quartzInformationService.getJobDetails("id")).thenReturn(quartzTaskInformationDTO);
		assertThrows(CustomSchedulerServiceException.class, () -> {
			QuartzServiceImpl.ResumeJob("id");
		});
	}

	@Test
	void testScheduleJob() {
		when(quartzSchedulerService.scheduleJob(quartzTaskInformationDTO)).thenReturn(true);
		when(quartzInformationService.getJobDetails("id")).thenReturn(quartzTaskInformationDTO);
		boolean val = QuartzServiceImpl.ScheduleJob("id");
		Mockito.verify(quartzInformationService).getJobDetails("id");
		Mockito.verify(quartzInformationService).updateJobStatus("id", JobStatus.UNFROZEN);
		assertEquals(val, true);
	}

	@Test
	void testScheduleJob_CustomSchedulerServiceException() {
		when(quartzSchedulerService.scheduleJob(quartzTaskInformationDTO))
				.thenThrow(CustomSchedulerServiceException.class);
		when(quartzInformationService.getJobDetails("id")).thenReturn(quartzTaskInformationDTO);
		assertThrows(CustomSchedulerServiceException.class, () -> {
			QuartzServiceImpl.ScheduleJob("id");
		});
	}

	@Test
	void TestRecordError() {
		QuartzTaskEventDTO qe = new QuartzTaskEventDTO();
		QuartzServiceImpl.recordError(qe, "id");
		Mockito.verify(quartzInformationService).recordEvent(qe, "id");
	}

}
