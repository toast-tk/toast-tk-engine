package io.toast.tk.dao.domain.impl.test.block;

import java.util.List;

import io.toast.tk.dao.domain.api.repository.IElement;
import io.toast.tk.dao.domain.api.repository.IRepository;

public interface IProject {

	List<ITestPlan> getTestPlans();

	List<IElement> getElements();

	List<IRepository> getRepositories();

}
