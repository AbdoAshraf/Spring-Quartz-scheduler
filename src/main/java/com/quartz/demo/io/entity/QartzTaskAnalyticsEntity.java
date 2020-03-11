package com.quartz.demo.io.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

	private LocalDateTime frozenTime;

	private LocalDateTime unfrozenTime;

}
