package com.quartz.demo.api.payload;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class JobDetails {

	@NotNull
	private String taskname;
	QuartzTaskConfig quartzTaskConfig;
}
