package com.mongo.test;

import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.synaptix.toast.dao.domain.impl.report.Project;
import com.synaptix.toast.dao.guice.MongoModule;
import com.synaptix.toast.dao.service.dao.access.project.ProjectDaoService;

public class TestMongo {

	public static void main(
		String[] args) {
		Injector in = Guice.createInjector(new MongoModule("10.23.252.131", 27017));
		ProjectDaoService.Factory repoFactory = in.getInstance(ProjectDaoService.Factory.class);
		ProjectDaoService service = repoFactory.create("play_db");
		List<Project> findAllReferenceProjects = service.findAllReferenceProjects();
	}

}
