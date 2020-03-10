package com.quartz.demo.io.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.quartz.demo.io.entity.QuartzTaskEventEntity;

public interface QuartzTaskErrorRepo extends PagingAndSortingRepository<QuartzTaskEventEntity, Long> {

}

//public interface QuartzTaskErrorRepo extends PagingAndSortingRepository<QuartzTaskErrorEntity, Long> {
//
//}