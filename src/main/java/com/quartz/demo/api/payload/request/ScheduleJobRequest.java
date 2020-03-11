package com.quartz.demo.api.payload.request;

import com.quartz.demo.api.payload.JobDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleJobRequest extends JobDetails {

	public ScheduleJobRequest() {
		super();
	}
}
