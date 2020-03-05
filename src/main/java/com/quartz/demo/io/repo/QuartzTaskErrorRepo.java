package com.quartz.demo.io.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.quartz.demo.io.entity.QuartzTaskError;

public interface QuartzTaskErrorRepo extends PagingAndSortingRepository<QuartzTaskError, Long> {

}
