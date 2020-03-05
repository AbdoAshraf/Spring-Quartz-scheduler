package com.quartz.demo.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class QuartzTaskError {
	
    private String  errorId;
    
    private LocalDateTime executeTime;
    
    private String failReason;
    
}
