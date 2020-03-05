package com.quartz.demo.io.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity(name = "QuartzTaskInformations")

public class QuartzTaskInformationsEntity {

	@Id
	@GeneratedValue
	private long id;

	@Column(length = 30, nullable = false)
	private String taskId;

	@Column(length = 15, nullable = false)
	private String taskName;

	@Column(length = 15, nullable = false)
	private String cornExp;

	private String frozenStatus;

	@Column(length = 15)
	private Long frozenTime;

	@Column(length = 15)
	private Long unfrozenTime;
	@Column(length = 15, nullable = false)
	private Long createTime;

	private Long lastmodifyTime;
	@Column(length = 15, nullable = false)
	private String sendType;

	@Column(length = 15, nullable = false)
	private String url;

	private String executeParamter;

	private long sucssesCount;

	private long failCount;

	@OneToMany(mappedBy = "QuartzTaskError", cascade = CascadeType.ALL)
	List<QuartzTaskError> quartzTaskErrorsList;

}