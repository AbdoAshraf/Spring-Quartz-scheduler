package com.quartz.demo.io.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuppressWarnings({ "serial" })
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
//@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractAuditableEntity<T extends Serializable> extends AbstractPersistableEntity {

	@CreatedDate
	LocalDate createdDate;

	@LastModifiedDate
	LocalDate lastModifiedDate;

	@CreatedBy
	T createdBy;

	@LastModifiedBy
	T lastModifiedBy;
}
