package com.synaptix.toast.dao.domain.impl.common;

import java.util.Date;

import com.github.jmkgreen.morphia.annotations.PrePersist;

public abstract class BasicMongoBean {

	protected Date lastUpdated = new Date();

	public String name;

	protected Date creationDate;

	public String getName() {
		return name;
	}

	public void setName(
		String name) {
		this.name = name;
	}

	@PrePersist
	public void prePersist() {
		if(creationDate == null) {
			creationDate = new Date();
		}
		lastUpdated = new Date();
	}
}
