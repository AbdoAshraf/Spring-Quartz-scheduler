package com.quartz.demo.io.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.quartz.demo.io.entity.QuartzTaskErrorEntity;

public interface QuartzTaskErrorRepo extends PagingAndSortingRepository<QuartzTaskErrorEntity, Long> {

}
