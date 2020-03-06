package com.quartz.demo.api.payload;

import com.quartz.demo.util.enums.SendType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class JobDetails {
	
	private String taskname;

	private String cornExp; //

	private SendType sendtype;

	private String url;

	private String executeparamter;

}
