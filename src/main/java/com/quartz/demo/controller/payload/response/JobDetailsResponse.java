package com.quartz.demo.controller.payload.response;

import java.util.List;

import com.quartz.demo.controller.payload.JobDetails;
import com.quartz.demo.dto.QartzTaskAnalyticsDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobDetailsResponse extends JobDetails {

	private String taskId;
	private List<QuartzTaskEventResponse> quartzTaskEventsList;
	QartzTaskAnalyticsDTO qartzTaskAnalyticsDTO;
}
