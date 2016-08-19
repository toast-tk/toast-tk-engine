package io.toast.tk.dao.domain.impl.repository;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Reference;

import io.toast.tk.dao.domain.api.repository.IRepository;
import io.toast.tk.dao.domain.impl.common.BasicEntityBean;

@Entity(value = "repository")
public class RepositoryImpl extends BasicEntityBean implements IRepository{

	public String cType;

	@Reference(ignoreMissing = true)
	public List<ElementImpl> rows = new ArrayList<>();
}
