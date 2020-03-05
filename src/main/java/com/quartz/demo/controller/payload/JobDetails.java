package com.quartz.demo.controller.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class JobDetails {
	private String taskname;

	private String cornExp; //

	private String sendtype;

	private String url;

	private String executeparamter;

}
