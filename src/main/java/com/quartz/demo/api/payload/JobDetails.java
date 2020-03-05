package com.quartz.demo.api.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class JobDetails {
	
	private String taskname;

	private String cornExp; //

	private String sendtype;

	private String url;

	private String executeparamter;

}
