package com.quartz.demo.dto;

import java.time.LocalDateTime;

import com.quartz.demo.util.enums.EventType;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor

public class QuartzTaskEvent {

	private String errorId;

	private EventType eventType;

	private LocalDateTime executeTime;

	private String reason;

}
