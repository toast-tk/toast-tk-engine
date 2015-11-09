package com.synaptix.toast.dao.domain.impl.repository;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.synaptix.toast.dao.domain.impl.common.BasicEntityBean;

@Entity(value = "elements", noClassnameStored = true)
public class ElementImpl extends BasicEntityBean {

	public String type;

	public String locator;

	public String method;

	public int position;

	public ElementImpl() {
		type = "";
		locator = "";
		name = "";
		method = "";
		position = 0;
	}
}
