package com.quartz.demo.controller;

import static org.springframework.http.ResponseEntity.created;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.quartz.demo.controller.payload.QuartzTaskConfig;
import com.quartz.demo.controller.payload.request.ScheduleJobRequest;
import com.quartz.demo.controller.payload.response.JobDetailsResponse;
import com.quartz.demo.controller.payload.response.ScheduleJobResponse;
import com.quartz.demo.dto.QuartzTaskConfigDTO;
import com.quartz.demo.dto.QuartzTaskInformationDTO;
import com.quartz.demo.service.QuartzService;

@RestController
public class SchedulerController {
	private ModelMapper modelMapper;
	private QuartzService quartzService;

	@Autowired
	SchedulerController(ModelMapper modelMapper, QuartzService quartzServiceInformation) {
		this.modelMapper = modelMapper;
		this.quartzService = quartzServiceInformation;
	}

	@PostMapping("/add-job")
	public ResponseEntity addJob(@RequestBody @Valid ScheduleJobRequest scheduleJobRequest,
			HttpServletRequest request) {
		QuartzTaskInformationDTO quartzTaskInformationDTO = this.quartzService
				.insertNewJob(this.modelMapper.map(scheduleJobRequest, QuartzTaskInformationDTO.class));

		return created(ServletUriComponentsBuilder.fromContextPath(request).path("/get-job/{id}")
				.buildAndExpand(quartzTaskInformationDTO.getTaskId()).toUri()).build();

	}

	@GetMapping("/get-job/{id}")
	public ResponseEntity<JobDetailsResponse> getJob(@PathVariable String id) {
		return new ResponseEntity<>(
				this.modelMapper.map(this.quartzService.getJobDetails(id), JobDetailsResponse.class), HttpStatus.OK);
	}

	@PutMapping("/scedule-job/{jobId}")
	public ResponseEntity<ScheduleJobResponse> addJob(@PathVariable String jobId) {
		this.quartzService.ScheduleJob(jobId);
		return new ResponseEntity<>(new ScheduleJobResponse(jobId, "job scheduled sucessfully"), HttpStatus.OK);

	}

	@PutMapping("/freez-job/{jobId}")
	public ResponseEntity<ScheduleJobResponse> freezJob(@PathVariable String jobId) {
		this.quartzService.freezJob(jobId);
		return new ResponseEntity<>(new ScheduleJobResponse(jobId, "job freezed sucessfully"), HttpStatus.OK);
	}

	@PutMapping("/resume-job/{jobId}")
	public ResponseEntity<ScheduleJobResponse> activateJob(@PathVariable String jobId) {
		this.quartzService.ResumeJob(jobId);
		return new ResponseEntity<>(new ScheduleJobResponse(jobId, "job resumed sucessfully"), HttpStatus.OK);

	}

	@PutMapping("update-job/{jobId}")
	public ResponseEntity<ScheduleJobResponse> updateJob(@PathVariable String jobId,
			@RequestBody @Valid QuartzTaskConfig quartzTaskConfig) {
		this.quartzService.updateJobConfig(jobId, this.modelMapper.map(quartzTaskConfig, QuartzTaskConfigDTO.class));
		return new ResponseEntity<>(new ScheduleJobResponse(jobId, "job updated sucessfully"), HttpStatus.OK);
	}

}
