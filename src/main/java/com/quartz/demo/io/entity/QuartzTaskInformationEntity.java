package com.quartz.demo.io.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class QuartzTaskInformationEntity extends AbstractAuditableEntity<Long> {
	private static final long serialVersionUID = 5313493413859894403L;

	@Column(nullable = false)
	private String taskId;

	@Column(nullable = false)
	private String taskName;

	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL)
	List<QuartzTaskEventEntity> quartzTaskErrorsList;

	@JoinColumn
	@OneToOne(cascade = CascadeType.ALL)
	QuartzTaskConfigEntity quartzTaskConfig;

	@JoinColumn
	@OneToOne(cascade = CascadeType.ALL)
	QartzTaskAnalyticsEntity qartzTaskAnalytics;

	@Override
	@GeneratedValue
	public Long getId() {
		return id;
	}

}