package com.quartz.demo.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.quartz.demo.dto.QuartzTaskInformation;
import com.quartz.demo.io.entity.QuartzTaskInformationEntity;
import com.quartz.demo.io.repo.QuartzTaskInformationRepo;
import com.quartz.demo.service.QuartzServiceInformation;
import com.quartz.demo.util.enums.JobStatus;

@Service
public class QuartzServiceInformationImpl implements QuartzServiceInformation {

	private QuartzTaskInformationRepo quartzTaskInformationRepo;
	private ModelMapper modelMapper;

	public QuartzServiceInformationImpl(QuartzTaskInformationRepo quartzTaskInformationRepo, ModelMapper modelMapper) {
		this.quartzTaskInformationRepo = quartzTaskInformationRepo;
		this.modelMapper = modelMapper;
	}

	@Override
	public QuartzTaskInformation insertNewJob(QuartzTaskInformation schedulejobRequest) {
		QuartzTaskInformation quartzTaskInformation = this.modelMapper.map(schedulejobRequest,
				QuartzTaskInformation.class);

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
	public QuartzTaskInformation updateJob(QuartzTaskInformation schedulejobRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuartzTaskInformation getJobDetails(String id) {
		return this.modelMapper.map(this.quartzTaskInformationRepo.findByTaskId(id), QuartzTaskInformation.class);
	}

}
