package com.quartz.demo.api.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleJobRequest extends JobDetails {

	public ScheduleJobRequest() {
		super();
	}
}
