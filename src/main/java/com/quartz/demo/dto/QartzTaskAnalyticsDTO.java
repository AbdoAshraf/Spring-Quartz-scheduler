package com.quartz.demo.dto;

import com.quartz.demo.util.enums.JobStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QartzTaskAnalyticsDTO {

////	private LocalDateTime frozenTime;
////
////	private LocalDateTime unfrozenTime;
////
////	private LocalDateTime lastmodifyTime;
//
//	private long sucssesCount;

	private JobStatus jobStatus;
	private long failCount = 0;

}
