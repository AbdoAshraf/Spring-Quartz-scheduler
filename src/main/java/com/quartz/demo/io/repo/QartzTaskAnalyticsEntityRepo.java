package com.quartz.demo.io.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.quartz.demo.io.entity.QartzTaskAnalyticsEntity;

public interface QartzTaskAnalyticsEntityRepo extends PagingAndSortingRepository<QartzTaskAnalyticsEntity, Long> {

}
