package io.toast.tk.dao.service.dao.access.test;

import java.util.List;

import javax.annotation.Nullable;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.mongodb.WriteConcern;

import io.toast.tk.dao.domain.impl.common.IServiceFactory;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.TestPage;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;
import io.toast.tk.dao.service.init.DbStarter;

public class TestPageDaoService extends AbstractMongoDaoService<TestPage> {

	public interface Factory extends IServiceFactory<TestPageDaoService>{
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

	public ITestPage getLastByName(final String name) {
		final Query<TestPage> query = createQuery();
		query.field("name").equal(name).order("-lastUpdated");
		final List<TestPage> asList = query.asList();
		return asList.get(0);
	}


	public ITestPage saveAsNewIteration(final ITestPage t) {
		t.setId(null);
		TestPage savedInstance = TestPageFromProxy.from(t);
		save(savedInstance);
		return savedInstance;
	}

	public ITestPage saveReference(final ITestPage t) {
		TestPage savedInstance = TestPageFromProxy.from(t);
		save(savedInstance);
		return savedInstance;		
	}
	
	public Key<TestPage> save(TestPage testPage){
		return super.save(testPage, WriteConcern.ACKNOWLEDGED);
		
	}
}