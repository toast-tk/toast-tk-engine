package io.toast.tk.dao.domain.impl.repository;

import java.util.List;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Reference;

import io.toast.tk.dao.domain.impl.common.BasicTaggableMongoBean;
import io.toast.tk.dao.domain.impl.report.TestPlanImpl;

@Entity(value = "projects", noClassnameStored = true)
public class ProjectImpl extends BasicTaggableMongoBean {

	@Id
	ObjectId id = new ObjectId();

	@Reference
	List<RepositoryImpl> repositories;
	
	@Reference
	List<ElementImpl> elements;
	
	@Reference
	List<TestPlanImpl> testPlans;
}
