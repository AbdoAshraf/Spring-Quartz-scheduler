package com.quartz.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class QuartzTaskError {
	
    private String  errorId;
    
    private Long executeTime;
    
    private String failReason;

    private Long lastModifytime;
    
}
