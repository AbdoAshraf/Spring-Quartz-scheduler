package com.quartz.demo.io.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.quartz.demo.util.enums.EventType;

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

public class QuartzTaskEventEntity {

	@Id
	@Column(nullable = false)
	private String errorId;

	private EventType eventType;

	private LocalDateTime executeTime;

	private String cause;
}
