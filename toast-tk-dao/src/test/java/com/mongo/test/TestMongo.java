package com.mongo.test;

import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.guice.MongoModule;
import io.toast.tk.dao.service.dao.access.plan.TestPlanDaoService;

public class TestMongo {

	public static void main(
		String[] args) {
		Injector in = Guice.createInjector(new MongoModule("10.23.252.131", 27017));
		TestPlanDaoService.Factory repoFactory = in.getInstance(TestPlanDaoService.Factory.class);
		TestPlanDaoService service = repoFactory.create("play_db");
		List<TestPlanImpl> findAllReferenceProjects = service.findAllReferenceProjects();
	}

}
