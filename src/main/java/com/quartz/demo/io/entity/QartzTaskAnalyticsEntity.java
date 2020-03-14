package com.quartz.demo.io.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.quartz.demo.util.enums.JobStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class QartzTaskAnalyticsEntity {

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private JobStatus jobStatus;

	@Column(nullable = false)
	private long failCount;

}
