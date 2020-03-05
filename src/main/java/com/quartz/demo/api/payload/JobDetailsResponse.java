package com.quartz.demo.api.payload;

import java.time.LocalDateTime;
import java.util.List;

import com.quartz.demo.util.enums.JobStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobDetailsResponse extends JobDetails{

	private String taskId;
	
	private JobStatus jobStatus;
	
	private LocalDateTime createTime;

	private LocalDateTime lastmodifyTime;

	private long sucssesCount;

	private long failCount;

	private List<QuartzTaskErrorResponse> quartzTaskErrorsList;

}
