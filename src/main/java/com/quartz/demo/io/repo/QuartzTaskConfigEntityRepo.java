package com.quartz.demo.io.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.quartz.demo.io.entity.QuartzTaskConfigEntity;

public interface QuartzTaskConfigEntityRepo extends PagingAndSortingRepository<QuartzTaskConfigEntity, Long> {

}
