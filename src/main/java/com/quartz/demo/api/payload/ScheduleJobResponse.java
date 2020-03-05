package com.quartz.demo.api.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleJobResponse {
    private String taskId;
    private String message;
}