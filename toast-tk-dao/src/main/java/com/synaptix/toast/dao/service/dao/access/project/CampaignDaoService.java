package com.synaptix.toast.dao.service.dao.access.project;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.synaptix.toast.dao.domain.impl.report.Campaign;
import com.synaptix.toast.dao.domain.impl.test.block.ICampaign;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.dao.service.dao.access.test.TestPageDaoService;
import com.synaptix.toast.dao.service.dao.common.AbstractMongoDaoService;
import com.synaptix.toast.dao.service.dao.common.CommonMongoDaoService;
import com.synaptix.toast.dao.service.init.DbStarter;

public class CampaignDaoService extends AbstractMongoDaoService<Campaign> {

	public interface Factory {

		CampaignDaoService create(
			@Assisted String dbName);
	}

	TestPageDaoService tService;

	@Inject
	public CampaignDaoService(
		DbStarter starter,
		CommonMongoDaoService cService,
		@Assisted String dbName,
		@Named("default_db") String default_db,
		TestPageDaoService.Factory tDaoServiceFactory) {
		super(Campaign.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
		tService = tDaoServiceFactory.create(dbName);
	}

	public ICampaign getByName(
		String name) {
		Query<Campaign> query = createQuery();
		query.field("name").equal(name).order("-iteration");
		return find(query).get();
	}

	public ICampaign saveAsNewIteration(
		Campaign c) {
		c.setId(null);
		List<ITestPage> savedTestCases = new ArrayList<ITestPage>(c.getTestCases().size());
		for(ITestPage t : c.getTestCases()) {
			savedTestCases.add(tService.saveAsNewIteration(t));
		}
		c.setTestCases(savedTestCases);
		save(c);
		return c;
	}

	public ICampaign saveReference(
		Campaign c) {
		for(ITestPage t : c.getTestCases()) {
			tService.saveReference(t);
		}
		save(c);
		return c;
	}

}
