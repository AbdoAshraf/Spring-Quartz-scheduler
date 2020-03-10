package com.quartz.demo.io.entity;

import java.io.Serializable;

public interface Loadable<T extends Serializable> {
	/**
	 *
	 * @return primary key value for this instance.
	 */
	T getId();
}
