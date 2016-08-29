package io.toast.tk.dao.domain.impl.common;

import java.util.Date;

import com.github.jmkgreen.morphia.annotations.PrePersist;

public abstract class BasicMongoBean {

	protected Date lastUpdated = new Date();

	protected String name;

	protected Date creationDate;

	protected String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@PrePersist
	public void prePersist() {
		if(creationDate == null) { //in constructor ?
			creationDate = new Date();
		}
		lastUpdated = new Date();
	}
}