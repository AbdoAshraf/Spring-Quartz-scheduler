package com.quartz.demo.controller.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ScheduleJobResponse {
    private String taskId;
    private String message;
}