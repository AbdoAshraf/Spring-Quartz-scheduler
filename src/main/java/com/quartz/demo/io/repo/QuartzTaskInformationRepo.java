package com.quartz.demo.io.repo;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.quartz.demo.io.entity.QuartzTaskInformationEntity;

@Repository
public interface QuartzTaskInformationRepo extends PagingAndSortingRepository<QuartzTaskInformationEntity, Long> {
	Optional<QuartzTaskInformationEntity> findByTaskId(String taskId);
}