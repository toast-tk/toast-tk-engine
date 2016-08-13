package com.mongo.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.service.dao.access.plan.TestPlanDaoService;

public class TestPlanTestCase extends EmbeddedMongoDBTestCase {
	
	static TestPlanDaoService service;
	
	@Test
	public void shouldFindNoTestPlanInEmbeddedMongoDb() {
		TestPlanDaoService.Factory repoFactory = injector.getInstance(TestPlanDaoService.Factory.class);
		service = repoFactory.create("play_db");		
		List<TestPlanImpl> testplans = service.findAllReferenceProjects();
		Assert.assertEquals(0, testplans.size());
	}

}
