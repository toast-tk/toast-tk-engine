package io.toast.tk.dao.service.dao.access.plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.mongodb.WriteConcern;

import io.toast.tk.dao.domain.impl.common.IServiceFactory;
import io.toast.tk.dao.domain.impl.report.Campaign;
import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.service.dao.access.test.TestPageDaoService;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;
import io.toast.tk.dao.service.init.DbStarter;

public class CampaignDaoService extends AbstractMongoDaoService<Campaign> {

	public interface Factory extends IServiceFactory<CampaignDaoService>{
	}

	TestPageDaoService tService;

	@Inject
	public CampaignDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		@Assisted final String databaseName,
		@Named("default_db") final String defaultDb,
		final TestPageDaoService.Factory tDaoServiceFactory)
	{
		super(Campaign.class, starter.getDatabaseByName(databaseName == null ? defaultDb : databaseName), cService);
		this.tService = tDaoServiceFactory.create(databaseName);
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
		if(Objects.nonNull(c.getTestCases())){
			final List<ITestPage> savedTestCases = new ArrayList<>(c.getTestCases().size());
			c.getTestCases().stream().forEach(t -> savedTestCases.add(tService.saveReference(t)));
			c.setTestCases(savedTestCases);
		}
		save(c);
		return c;
	}
	
	public Key<Campaign> save(Campaign campaign){
		return super.save(campaign, WriteConcern.ACKNOWLEDGED);
		
	}
}