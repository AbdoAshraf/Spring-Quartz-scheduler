package com.quartz.demo.api;

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

import com.quartz.demo.api.payload.JobDetailsResponse;
import com.quartz.demo.api.payload.ScheduleJobRequest;
import com.quartz.demo.api.payload.ScheduleJobResponse;
import com.quartz.demo.dto.QuartzTaskInformation;
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
	public ResponseEntity<ScheduleJobResponse> addJob(@RequestBody @Valid ScheduleJobRequest scheduleJobRequest) {
		QuartzTaskInformation quartzTaskInformation = this.quartzService
				.insertNewJob(this.modelMapper.map(scheduleJobRequest, QuartzTaskInformation.class));
		ScheduleJobResponse scheduleJobResponse = this.modelMapper.map(quartzTaskInformation,
				ScheduleJobResponse.class);
		return new ResponseEntity<>(scheduleJobResponse, HttpStatus.OK);

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

}
