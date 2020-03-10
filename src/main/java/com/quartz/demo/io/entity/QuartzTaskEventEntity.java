package com.quartz.demo.io.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.quartz.demo.util.enums.EventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@AllArgsConstructor
public class QuartzTaskEventEntity implements Serializable {
	private static final long serialVersionUID = -3151715408586880484L;

	@Id
	@GeneratedValue
	private long id;

	private EventType eventType;

	private String errorId;

	private LocalDateTime executeTime;

	private String reason;

}
