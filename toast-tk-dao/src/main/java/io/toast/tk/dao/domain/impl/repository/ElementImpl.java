package io.toast.tk.dao.domain.impl.repository;

import com.github.jmkgreen.morphia.annotations.Entity;

import io.toast.tk.dao.domain.impl.common.BasicEntityBean;

@Entity(value = "elements", noClassnameStored = true)
public class ElementImpl extends BasicEntityBean {

	public String type;

	public String locator;

	public String method;

	public int position;

	public ElementImpl() {
		this.type = "";
		this.locator = "";
		this.name = "";
		this.method = "";
		this.position = 0;
	}
}