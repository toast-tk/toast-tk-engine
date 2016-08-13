package io.toast.tk.runtime.dao;

import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.guice.MongoModule;
import io.toast.tk.dao.service.dao.access.plan.TestPlanDaoService;

public class DAOManager {

	private Injector mongoServiceInjector;

	private TestPlanDaoService.Factory projectFactory;

	private TestPlanDaoService projectService;

	private static DAOManager INSTANCE;

	private DAOManager(
		final String mongoHost, 
		final int mongoPort
	) {
		this.mongoServiceInjector = Guice.createInjector(new MongoModule(mongoHost, mongoPort));
		this.projectFactory = mongoServiceInjector.getInstance(TestPlanDaoService.Factory.class);
		this.projectService = projectFactory.create("test_project_db");
	}
	
	public static synchronized DAOManager getInstance(
		final String mongoHost, 
		final int mongoPort
	) {
		if(INSTANCE == null) {
			INSTANCE = new DAOManager(mongoHost, mongoPort);
		}
		return INSTANCE;
	}
	
	public static synchronized DAOManager getInstance() {
		if(INSTANCE == null) {
			throw new RuntimeException("Mongo Host not provided !");
		}
		return INSTANCE;
	}

	TestPlanDaoService getProjectDaoService() {
		return projectService;
	}
	
	public static TestPlanImpl getLastProjectByName(final String projectName) {
		return getInstance().getProjectDaoService().getLastByName(projectName);
	}

	public static TestPlanImpl getReferenceProjectByName(final String projectName) {
		return getInstance().getProjectDaoService().getReferenceProjectByName(projectName);
	}
	
	public static void saveProject(final TestPlanImpl project) {
		getInstance().getProjectDaoService().saveNewIteration(project);
	}

	public static List<TestPlanImpl> getProjectHistory(final TestPlanImpl project) {
		return getInstance().getProjectDaoService().getProjectHistory(project);
	}
}