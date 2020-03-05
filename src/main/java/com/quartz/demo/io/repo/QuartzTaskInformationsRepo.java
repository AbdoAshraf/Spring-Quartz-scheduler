package com.quartz.demo.io.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.quartz.demo.io.entity.QuartzTaskInformationsEntity;

@Repository
public interface QuartzTaskInformationsRepo extends PagingAndSortingRepository<QuartzTaskInformationsEntity, Long> {
	QuartzTaskInformationsEntity findByTaskId(String taskId);
}