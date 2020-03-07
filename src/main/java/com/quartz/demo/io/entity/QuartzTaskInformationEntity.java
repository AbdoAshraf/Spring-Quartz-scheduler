package com.quartz.demo.io.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.quartz.demo.dto.QuartzTaskError;
import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.JobStatus;
import com.quartz.demo.util.enums.SendType;
import com.quartz.demo.util.enums.SimpleMisfire;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity

public class QuartzTaskInformationEntity implements Serializable {
	private static final long serialVersionUID = 5313493413859894403L;

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

	private SendType sendType;

	private String url;

	private String executeParamter;

	private long sucssesCount;

	private long failCount;

	private String triggerType;
	private int triggerPriority;
	private SimpleMisfire simpleMisfire;
	private CronMisfire cronMisfire;
	private int intervalInSeconds = 0;

	@OneToMany(cascade = CascadeType.ALL)
	List<QuartzTaskErrorEntity> quartzTaskErrorsList;

}