package io.toast.tk.runtime.dao;

import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.toast.tk.dao.domain.impl.report.Project;
import io.toast.tk.dao.guice.MongoModule;
import io.toast.tk.dao.service.dao.access.project.ProjectDaoService;

public class DAOManager {

	private Injector mongoServiceInjector;

	private ProjectDaoService.Factory projectFactory;

	private ProjectDaoService projectService;

	private static DAOManager INSTANCE;

	private DAOManager(
		final String mongoHost, 
		final int mongoPort
	) {
		this.mongoServiceInjector = Guice.createInjector(new MongoModule(mongoHost, mongoPort));
		this.projectFactory = mongoServiceInjector.getInstance(ProjectDaoService.Factory.class);
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

	ProjectDaoService getProjectDaoService() {
		return projectService;
	}
	
	public static Project getLastProjectByName(final String projectName) {
		return getInstance().getProjectDaoService().getLastByName(projectName);
	}

	public static Project getReferenceProjectByName(final String projectName) {
		return getInstance().getProjectDaoService().getReferenceProjectByName(projectName);
	}
	
	public static void saveProject(final Project project) {
		getInstance().getProjectDaoService().saveNewIteration(project);
	}

	public static List<Project> getProjectHistory(final Project project) {
		return getInstance().getProjectDaoService().getProjectHistory(project);
	}
}