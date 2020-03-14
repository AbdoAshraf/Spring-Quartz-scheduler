package com.quartz.demo.controller.payload.request;

import com.quartz.demo.controller.payload.JobDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleJobRequest extends JobDetails {

	public ScheduleJobRequest() {
		super();
	}
}
