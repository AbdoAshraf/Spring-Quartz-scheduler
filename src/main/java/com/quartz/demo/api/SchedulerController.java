package com.quartz.demo.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.quartz.demo.api.payload.JobDetailsResponse;
import com.quartz.demo.api.payload.ScheduleJobRequest;
import com.quartz.demo.api.payload.ScheduleJobResponse;
import com.quartz.demo.dto.QuartzTaskInformation;
import com.quartz.demo.service.QuartzServiceInformation;

@RestController
public class SchedulerController {
	private ModelMapper modelMapper;
	private QuartzServiceInformation quartzServiceInformation;

	@Autowired
	SchedulerController(ModelMapper modelMapper, QuartzServiceInformation quartzServiceInformation) {
		this.modelMapper = modelMapper;
		this.quartzServiceInformation = quartzServiceInformation;
	}

	@PostMapping("/add-job")
	public ResponseEntity<ScheduleJobResponse> addJob(@RequestBody ScheduleJobRequest scheduleJobRequest) {
		QuartzTaskInformation quartzTaskInformation = this.quartzServiceInformation
				.insertNewJob(this.modelMapper.map(scheduleJobRequest, QuartzTaskInformation.class));
		ScheduleJobResponse scheduleJobResponse = this.modelMapper.map(quartzTaskInformation,
				ScheduleJobResponse.class);
		return new ResponseEntity<>(scheduleJobResponse, HttpStatus.OK);

	}
	
	
	@GetMapping("/get-job/{id}")
	public ResponseEntity<JobDetailsResponse> getJob(@PathVariable String id) {
		return new ResponseEntity<>(this.modelMapper.map(this.quartzServiceInformation.getJobDetails(id)
				,JobDetailsResponse.class),HttpStatus.OK);
	}

}
