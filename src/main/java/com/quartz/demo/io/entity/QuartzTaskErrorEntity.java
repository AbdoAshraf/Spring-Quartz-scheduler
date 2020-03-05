package com.quartz.demo.io.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data

@Entity
public class QuartzTaskErrorEntity {
	@Id
	@GeneratedValue
	private long id;
	
	private String errorId;

	private String executeTime;

	private String failReason;

}
