package com.mongo.test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.guice.MongoModule;
import io.toast.tk.dao.service.dao.access.plan.TestPlanDaoService;

public class GroupTestCase extends EmbeddedMongoDBTestCase {

	//NOOP
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new MongoModule("localhost", 27017));
		TestPlanDaoService.Factory f = injector.getInstance(TestPlanDaoService.Factory.class);
		TestPlanDaoService service = f.create("test_project_db");
		TestPlanImpl testPlan = service.getReferenceProjectByName("TestPlan 5");
		try {
			service.updateTemplateFromTestPlan(testPlan);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
