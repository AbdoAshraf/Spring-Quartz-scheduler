package com.quartz.demo.io.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class QuartzTaskErrorEntity implements Serializable{
	private static final long serialVersionUID = -3151715408586880484L;

	@Id
	@GeneratedValue
	private long id;
	
	private String errorId;

	private LocalDateTime executeTime;

	private String failReason;

}
