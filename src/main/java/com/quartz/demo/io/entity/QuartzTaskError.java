package com.quartz.demo.io.entity;

import javax.persistence.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data

@Entity(name = "QuartzTaskError")
public class QuartzTaskError {
	private String errorId;

	private Long executeTime;

	private String failReason;

}
