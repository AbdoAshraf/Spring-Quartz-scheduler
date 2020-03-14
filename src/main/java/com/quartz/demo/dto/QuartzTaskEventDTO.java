package com.quartz.demo.dto;

import java.time.LocalDateTime;

import com.quartz.demo.util.enums.EventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuartzTaskEventDTO {

	private String errorId;

	private EventType eventType;

	private LocalDateTime executeTime;

	private String cause;
}
