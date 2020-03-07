package com.quartz.demo.service.info;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.quartz.demo.dto.QuartzTaskError;
import com.quartz.demo.dto.QuartzTaskInformation;
import com.quartz.demo.exception.InfoServiceExcseption;
import com.quartz.demo.io.entity.QuartzTaskErrorEntity;
import com.quartz.demo.io.entity.QuartzTaskInformationEntity;
import com.quartz.demo.io.repo.QuartzTaskInformationRepo;
import com.quartz.demo.util.enums.JobStatus;

@Service
public class QuartzInformationServiceImpl implements QuartzInformationService {

	private QuartzTaskInformationRepo quartzTaskInformationRepo;
	private ModelMapper modelMapper;

	public QuartzInformationServiceImpl(QuartzTaskInformationRepo quartzTaskInformationRepo, ModelMapper modelMapper) {
		this.quartzTaskInformationRepo = quartzTaskInformationRepo;
		this.modelMapper = modelMapper;
	}

	@Override
	public QuartzTaskInformation insertNewJob(QuartzTaskInformation quartzTaskInformation) {
		// validation step
		QuartzTaskInformationEntity entity = this.modelMapper.map(quartzTaskInformation,
				QuartzTaskInformationEntity.class);
		entity.setTaskId(UUID.randomUUID().toString());
		entity.setCreateTime(LocalDateTime.now());
		entity.setJobStatus(JobStatus.FROZEN);
		entity = this.quartzTaskInformationRepo.save(entity);
		return this.modelMapper.map(entity, QuartzTaskInformation.class);
	}

	@Override
	public QuartzTaskInformation updateJob(QuartzTaskInformation quartzTaskInformation) {
		if (this.quartzTaskInformationRepo.findByTaskId(quartzTaskInformation.getTaskId()) == null)
			throw new InfoServiceExcseption("invalid task id");

		QuartzTaskInformationEntity entity = this.quartzTaskInformationRepo
				.findByTaskId(quartzTaskInformation.getTaskId());
		entity.setFrozenTime(quartzTaskInformation.getFrozenTime());
		entity.setLastmodifyTime(quartzTaskInformation.getLastmodifyTime());
		entity.setJobStatus(quartzTaskInformation.getJobStatus());
		entity.setUnfrozenTime(quartzTaskInformation.getUnfrozenTime());
		entity = this.quartzTaskInformationRepo.save(entity);
		return this.modelMapper.map(entity, QuartzTaskInformation.class);
	}

	@Override
	public QuartzTaskInformation getJobDetails(String id) {
		if (this.quartzTaskInformationRepo.findByTaskId(id) == null)
			throw new InfoServiceExcseption("invalid task id");
		return this.modelMapper.map(this.quartzTaskInformationRepo.findByTaskId(id), QuartzTaskInformation.class);
	}

	@Override
	public void recordError(QuartzTaskError quartzTaskError, String taskId) {
		if (this.quartzTaskInformationRepo.findByTaskId(taskId) == null)
			throw new InfoServiceExcseption("invalid task id recordError");
		QuartzTaskInformationEntity entity = this.quartzTaskInformationRepo.findByTaskId(taskId);
		entity.getQuartzTaskErrorsList().add(this.modelMapper.map(quartzTaskError, QuartzTaskErrorEntity.class));
		entity.setFailCount(entity.getFailCount() + 1);
		this.quartzTaskInformationRepo.save(entity);
	}

}
