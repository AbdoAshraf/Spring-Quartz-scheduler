package com.quartz.demo.service.info;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.quartz.demo.dto.QuartzTaskError;
import com.quartz.demo.dto.QuartzTaskInformation;
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
		// quartzTaskInformation.setTaskId(UUID.randomUUID().toString());
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
		QuartzTaskInformationEntity entity = this.modelMapper.map(quartzTaskInformation,
				QuartzTaskInformationEntity.class);
		entity = this.quartzTaskInformationRepo.save(entity);
		return this.modelMapper.map(entity, QuartzTaskInformation.class);
	}

	@Override
	public QuartzTaskInformation getJobDetails(String id) {
		return this.modelMapper.map(this.quartzTaskInformationRepo.findByTaskId(id), QuartzTaskInformation.class);
	}

	@Override
	public void recordError(Exception e, String taskId) {
		QuartzTaskInformationEntity entity=this.quartzTaskInformationRepo.findByTaskId(taskId);
		entity.setFailCount(entity.getFailCount()+1);
		QuartzTaskError quartzTaskError= new QuartzTaskError();
		quartzTaskError.setErrorId(UUID.randomUUID().toString());
		quartzTaskError.setExecuteTime(LocalDateTime.now());
		quartzTaskError.setFailReason(e.getMessage());
		entity.getQuartzTaskErrorsList().add(this.modelMapper.map(quartzTaskError, QuartzTaskErrorEntity.class));
	}

}
