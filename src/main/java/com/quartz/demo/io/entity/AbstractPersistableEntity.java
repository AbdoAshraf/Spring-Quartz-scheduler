package com.quartz.demo.io.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("serial")
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractPersistableEntity implements Loadable<Long>, Serializable {

	@Id
	@GeneratedValue

	protected Long id;

	@Version
	private Long version;

	public Long getVersion() {
		return version;
	}
}
