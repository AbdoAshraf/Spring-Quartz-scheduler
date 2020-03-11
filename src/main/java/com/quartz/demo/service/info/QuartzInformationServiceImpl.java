package com.quartz.demo.service.info;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quartz.demo.dto.QuartzTaskEvent;
import com.quartz.demo.dto.QuartzTaskInformation;
import com.quartz.demo.exception.InfoServiceExcseption;
import com.quartz.demo.io.entity.QuartzTaskEventEntity;
import com.quartz.demo.io.entity.QuartzTaskInformationEntity;
import com.quartz.demo.io.repo.QuartzTaskInformationRepo;
import com.quartz.demo.util.enums.JobStatus;

@Service
public class QuartzInformationServiceImpl implements QuartzInformationService {

	private QuartzTaskInformationRepo quartzTaskInformationRepo;
	private ModelMapper modelMapper;

	@Autowired
	public QuartzInformationServiceImpl(QuartzTaskInformationRepo quartzTaskInformationRepo, ModelMapper modelMapper) {
		this.quartzTaskInformationRepo = quartzTaskInformationRepo;
		this.modelMapper = modelMapper;
	}

	@Override
	public QuartzTaskInformation insertNewJob(QuartzTaskInformation quartzTaskInformation) {
		QuartzTaskInformationEntity entity = this.modelMapper.map(quartzTaskInformation,
				QuartzTaskInformationEntity.class);
		entity.setTaskId(UUID.randomUUID().toString());
		entity.setJobStatus(JobStatus.FROZEN);
		// entity.getQartzTaskAnalytics().setFrozenTime(LocalDateTime.now());
		entity = this.quartzTaskInformationRepo.save(entity);
		return this.modelMapper.map(entity, QuartzTaskInformation.class);
	}

	@Override
	public QuartzTaskInformation getJobDetails(String id) {
		return this.modelMapper.map(this.quartzTaskInformationRepo.findByTaskId(id)
				.orElseThrow(() -> new InfoServiceExcseption("invalid task id")), QuartzTaskInformation.class);
	}

	private QuartzTaskInformationEntity getJob(String id) {
		return this.quartzTaskInformationRepo.findByTaskId(id)
				.orElseThrow(() -> new InfoServiceExcseption("invalid task id"));
	}

	@Override
	public void recordError(QuartzTaskEvent quartzTaskError, String taskId) {
		QuartzTaskInformationEntity entity = getJob(taskId);
		long count = entity.getFailCount() + 1;
		entity.setFailCount(count);
		entity.getQuartzTaskEventsList().add(this.modelMapper.map(quartzTaskError, QuartzTaskEventEntity.class));
		this.quartzTaskInformationRepo.save(entity);
	}

	@Override
	public QuartzTaskInformation updateJob(QuartzTaskInformation quartzTaskInformation) {
		return null;
	}
}
