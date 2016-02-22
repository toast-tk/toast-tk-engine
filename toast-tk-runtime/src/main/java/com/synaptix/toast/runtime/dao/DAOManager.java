package com.synaptix.toast.runtime.dao;

import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.synaptix.toast.dao.domain.impl.report.Project;
import com.synaptix.toast.dao.guice.MongoModule;
import com.synaptix.toast.dao.service.dao.access.project.ProjectDaoService;

public class DAOManager {

	private static Injector MONGO_SERVICE_INJECTOR;

	private static ProjectDaoService.Factory PROJECT_FACTORY;

	private static ProjectDaoService PROJECT_SERVICE;

	private static DAOManager INSTANCE;

	private DAOManager(
		final String mongoHost, 
		final int mongoPort
	) {
		MONGO_SERVICE_INJECTOR = Guice.createInjector(new MongoModule(mongoHost, mongoPort));
		PROJECT_FACTORY = MONGO_SERVICE_INJECTOR.getInstance(ProjectDaoService.Factory.class);
		PROJECT_SERVICE = PROJECT_FACTORY.create("test_project_db");
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
	
	public static synchronized DAOManager getInstance() throws IllegalAccessException {
		if(INSTANCE == null) {
			throw new IllegalAccessException("Mongo Host not provided !");
		}
		return INSTANCE;
	}

	public static Project getLastProjectByName(final String projectName) {
		return PROJECT_SERVICE.getLastByName(projectName);
	}

	public static Project getReferenceProjectByName(final String projectName) {
		return PROJECT_SERVICE.getReferenceProjectByName(projectName);
	}
	
	public static void saveProject(final Project project) {
		PROJECT_SERVICE.saveNewIteration(project);
	}

	public static List<Project> getProjectHistory(final Project project) {
		return PROJECT_SERVICE.getProjectHistory(project);
	}
}