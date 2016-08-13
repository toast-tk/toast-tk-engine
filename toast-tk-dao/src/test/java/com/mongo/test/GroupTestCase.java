package com.mongo.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.service.dao.access.plan.TestPlanDaoService;

public class GroupTestCase extends EmbeddedMongoDBTestCase {

	@Test
	public void shouldFindNoTestPlanInEmbeddedMongoDb() {
		TestPlanDaoService.Factory repoFactory = injector.getInstance(TestPlanDaoService.Factory.class);
		TestPlanDaoService service = repoFactory.create("play_db");
		List<TestPlanImpl> testplans = service.findAllReferenceProjects();
		Assert.assertEquals(0, testplans.size());
	}
}
