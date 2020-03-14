package com.quartz.demo.io.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.quartz.demo.util.enums.CronMisfire;
import com.quartz.demo.util.enums.SendType;
import com.quartz.demo.util.enums.SimpleMisfire;
import com.quartz.demo.util.enums.TriggerType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class QuartzTaskConfigEntity {

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private SendType sendType;

	private String url;

	private String cornExp;

	private String executeParamter;

	private TriggerType triggerType;

	private int triggerPriority;

	private int repeatCount;
	private SimpleMisfire simpleMisfire;

	private CronMisfire cronMisfire;

	private int intervalInSeconds = 0;

}
