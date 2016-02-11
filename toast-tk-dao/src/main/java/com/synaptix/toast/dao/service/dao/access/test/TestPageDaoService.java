package com.synaptix.toast.dao.service.dao.access.test;

import javax.annotation.Nullable;

import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.dao.domain.impl.test.block.TestPage;
import com.synaptix.toast.dao.service.dao.common.AbstractMongoDaoService;
import com.synaptix.toast.dao.service.dao.common.CommonMongoDaoService;
import com.synaptix.toast.dao.service.init.DbStarter;

public class TestPageDaoService extends AbstractMongoDaoService<TestPage> {

	public interface Factory {

		TestPageDaoService create(final @Nullable @Assisted String dbName);
	}

	@Inject
	public TestPageDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		final @Nullable @Assisted String dbName,
		final @Named("default_db") String default_db
	) {
		super(TestPage.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
	}

	public ITestPage getByName(final String name) {
		final Query<TestPage> query = createQuery();
		query.field("name").equals(name);
		return query.get();
	}

	public ITestPage saveAsNewIteration(final ITestPage t) {
		t.setId(null);
		TestPage savedInstance = TestPageFromProxy.from(t);
		save(savedInstance);
		return savedInstance;
	}

	public ITestPage saveReference(final ITestPage t) {
		save((TestPage) t);
		return t;		
	}
}