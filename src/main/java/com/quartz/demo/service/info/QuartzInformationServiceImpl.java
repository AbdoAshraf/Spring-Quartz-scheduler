package com.quartz.demo.service.info;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quartz.demo.dto.QuartzTaskConfigDTO;
import com.quartz.demo.dto.QuartzTaskEventDTO;
import com.quartz.demo.dto.QuartzTaskInformationDTO;
import com.quartz.demo.exception.InfoServiceExcseption;
import com.quartz.demo.io.entity.QuartzTaskConfigEntity;
import com.quartz.demo.io.entity.QuartzTaskEventEntity;
import com.quartz.demo.io.entity.QuartzTaskInformationEntity;
import com.quartz.demo.io.repo.QuartzTaskInformationRepo;
import com.quartz.demo.util.enums.EventType;
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
	public QuartzTaskInformationDTO insertNewJob(QuartzTaskInformationDTO quartzTaskInformationDTO) {
		QuartzTaskInformationEntity entity = this.modelMapper.map(quartzTaskInformationDTO,
				QuartzTaskInformationEntity.class);
		entity.setTaskId(UUID.randomUUID().toString());
		entity.getQartzTaskAnalytics().setJobStatus(JobStatus.FROZEN);
		entity = this.quartzTaskInformationRepo.save(entity);
		return this.modelMapper.map(entity, QuartzTaskInformationDTO.class);
	}

	@Override
	public QuartzTaskInformationDTO getJobDetails(String id) {
		return this.modelMapper.map(this.getJob(id), QuartzTaskInformationDTO.class);
	}

	private QuartzTaskInformationEntity getJob(String id) {
		QuartzTaskInformationEntity entity = this.quartzTaskInformationRepo.findByTaskId(id);
		if (entity == null)
			throw new InfoServiceExcseption("invalid task id");
		return entity;
	}

	@Override
	public void recordEvent(QuartzTaskEventDTO quartzTaskError, String taskId) {
		QuartzTaskInformationEntity entity = getJob(taskId);
		if (quartzTaskError.getEventType() == EventType.Error) {
			long failCount = entity.getQartzTaskAnalytics().getFailCount() + 1;
			entity.getQartzTaskAnalytics().setFailCount(failCount);
		}
		entity.getQuartzTaskEventsList().add(this.modelMapper.map(quartzTaskError, QuartzTaskEventEntity.class));
		this.quartzTaskInformationRepo.save(entity);
	}

	@Override
	public void updateJobStatus(String taskId, JobStatus jobStatus) {
		if (jobStatus == null)
			throw new InfoServiceExcseption("invalid jobStatus");
		QuartzTaskInformationEntity entity = getJob(taskId);
		entity.getQartzTaskAnalytics().setJobStatus(jobStatus);
		this.quartzTaskInformationRepo.save(entity);
	}

	@Override
	public void updateJobConfig(String taskId, QuartzTaskConfigDTO quartzTaskConfigDTO) {
		QuartzTaskInformationEntity entity = getJob(taskId);
		entity.setQuartzTaskConfig(this.modelMapper.map(quartzTaskConfigDTO, QuartzTaskConfigEntity.class));
		this.quartzTaskInformationRepo.save(entity);
	}
}
