package io.toast.tk.dao.domain.impl.repository;

import java.util.List;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Reference;

import io.toast.tk.dao.domain.api.repository.IElement;
import io.toast.tk.dao.domain.api.repository.IRepository;
import io.toast.tk.dao.domain.impl.common.BasicTaggableMongoBean;
import io.toast.tk.dao.domain.impl.test.block.IProject;
import io.toast.tk.dao.domain.impl.test.block.ITestPlan;

@Entity(value = "projects", noClassnameStored = true)
public class ProjectImpl extends BasicTaggableMongoBean implements IProject {

	@Id
	ObjectId id = new ObjectId();

	@Reference(ignoreMissing=true)
	List<IRepository> repositories;
	
	@Reference(ignoreMissing=true)
	List<IElement> elements;
	
	@Reference(ignoreMissing=true)
	List<ITestPlan> testPlans;

	@Override
	public List<IRepository> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<IRepository> repositories) {
		this.repositories = repositories;
	}

	@Override
	public List<IElement> getElements() {
		return elements;
	}

	public void setElements(List<IElement> elements) {
		this.elements = elements;
	}

	@Override
	public List<ITestPlan> getTestPlans() {
		return testPlans;
	}

	public void setTestPlans(List<ITestPlan> testPlans) {
		this.testPlans = testPlans;
	}
	
	
}
