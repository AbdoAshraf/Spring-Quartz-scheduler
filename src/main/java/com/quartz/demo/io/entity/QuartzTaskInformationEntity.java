package com.quartz.demo.io.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.quartz.demo.util.enums.JobStatus;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity

public class QuartzTaskInformationEntity {

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private String taskId;

	@Column(nullable = false)
	private String taskName;

	private String cornExp;

	private JobStatus jobStatus;
	private LocalDateTime frozenTime;

	private LocalDateTime unfrozenTime;

	private LocalDateTime createTime;

	private LocalDateTime lastmodifyTime;

	private String sendType;

	private String url;

	private String executeParamter;

	private long sucssesCount;

	private long failCount;

	@OneToMany(cascade = CascadeType.ALL)
	List<QuartzTaskErrorEntity> quartzTaskErrorsList;

}