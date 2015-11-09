package com.synaptix.toast.dao.domain.impl.repository;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.synaptix.toast.dao.domain.impl.common.BasicEntityBean;

@Entity(value = "repository", noClassnameStored = true)
public class RepositoryImpl extends BasicEntityBean {

	public String type;

	@Reference(ignoreMissing = true)
	public List<ElementImpl> rows = new ArrayList<ElementImpl>();
}
