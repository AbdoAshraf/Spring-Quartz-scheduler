package com.quartz.demo.io.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import org.springframework.data.annotation.CreatedDate;

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
public class QartzTaskAnalyticsEntity extends AbstractPersistableEntity {

	private LocalDateTime frozenTime;

	private LocalDateTime unfrozenTime;

	@CreatedDate
	private LocalDateTime createTime;

	private long failCount;

	@Column(nullable = false)
	private JobStatus jobStatus;

	@Override
	@GeneratedValue
	public Long getId() {
		return id;
	}

}
