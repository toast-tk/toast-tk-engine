package com.synaptix.toast.runtime.dao;

import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.synaptix.toast.dao.domain.impl.report.Project;
import com.synaptix.toast.dao.guice.MongoModule;
import com.synaptix.toast.dao.service.dao.access.project.ProjectDaoService;

public class DAOManager {

	private static Injector mongoServiceInjector;

	private static ProjectDaoService.Factory pfactory;

	private static ProjectDaoService service;

	private static DAOManager instance;

	private DAOManager(String mongoHost, int mongoPort) {
		mongoServiceInjector = Guice.createInjector(new MongoModule(mongoHost, mongoPort));
		pfactory = mongoServiceInjector.getInstance(ProjectDaoService.Factory.class);
		service = pfactory.create("test_project_db");
	}
	
	public static synchronized DAOManager getInstance(String mongoHost, int mongoPort) {
		if(instance == null) {
			instance = new DAOManager(mongoHost, mongoPort);
		}
		return instance;
	}
	
	public static synchronized DAOManager getInstance() {
		if(instance == null) {
			throw new IllegalAccessError("Mongo Host not provided !");
		}
		return instance;
	}

	public Project getLastProjectByName(
		String projectName) {
		return service.getLastByName(projectName);
	}

	public Project getReferenceProjectByName(
		String projectName){
		return service.getReferenceProjectByName(projectName);
	}
	
	
	public void saveProject(
		Project project) {
		service.saveNewIteration(project);
	}

	public List<Project> getProjectHistory(
		Project project) {
		return service.getProjectHistory(project);
	}
}
