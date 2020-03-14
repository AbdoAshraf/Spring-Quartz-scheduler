package com.quartz.demo.io.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class QuartzTaskInformationEntity {
	private static final long serialVersionUID = 5313493413859894403L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private String taskId;

	@Column(nullable = false)
	private String taskName;

	@Column(nullable = false)
	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	List<QuartzTaskEventEntity> quartzTaskEventsList;

	@JoinColumn
	@OneToOne(cascade = CascadeType.ALL)
	QuartzTaskConfigEntity quartzTaskConfig;

	@JoinColumn
	@OneToOne(cascade = CascadeType.ALL)
	QartzTaskAnalyticsEntity QartzTaskAnalytics;

}