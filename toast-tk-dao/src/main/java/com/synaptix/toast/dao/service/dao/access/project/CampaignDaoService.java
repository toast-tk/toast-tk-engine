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

		CampaignDaoService create(final @Assisted String dbName);
	}

	TestPageDaoService tService;

	@Inject
	public CampaignDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		final @Assisted String dbName,
		final @Named("default_db") String default_db,
		final TestPageDaoService.Factory tDaoServiceFactory)
	{
		super(Campaign.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
		this.tService = tDaoServiceFactory.create(dbName);
	}

	public ICampaign getByName(final String name) {
		final Query<Campaign> query = createQuery();
		query.field("name").equal(name).order("-iteration");
		return find(query).get();
	}

	public ICampaign saveAsNewIteration(final Campaign c) {
		c.setId(null);
		final List<ITestPage> savedTestCases = new ArrayList<>(c.getTestCases().size());
		c.getTestCases().stream().forEach(t -> savedTestCases.add(tService.saveAsNewIteration(t)));
		c.setTestCases(savedTestCases);
		save(c);
		return c;
	}

	public ICampaign saveReference(final Campaign c) {
		c.getTestCases().stream().forEach(t -> tService.saveReference(t));
		save(c);
		return c;
	}
}