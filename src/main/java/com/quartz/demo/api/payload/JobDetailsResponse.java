package com.quartz.demo.api.payload;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobDetailsResponse extends JobDetails {

	private String taskId;
	private List<QuartzTaskErrorResponse> quartzTaskEventsList;

}
