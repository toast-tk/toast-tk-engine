package io.toast.tk.dao.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import io.toast.tk.dao.config.DaoConfig;
import io.toast.tk.dao.config.DaoConfigProvider;
import io.toast.tk.dao.service.dao.common.EntityCollectionManager;
import io.toast.tk.dao.service.init.DbStarter;
import io.toast.tk.dao.service.init.MongoDefaultStarterImpl;

public class MongoModule extends AbstractModule {

	private final String mongoHost;

	private final int mongoPort;

	public MongoModule() {
		this.mongoHost = "";
		this.mongoPort = -1;
	}

	public MongoModule(
		final String mongoHost,
		final int mongoPort
	) {
		this.mongoHost = mongoHost;
		this.mongoPort = mongoPort;
	}

	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("MongoHost")).to(mongoHost);
		bindConstant().annotatedWith(Names.named("MongoPort")).to(mongoPort);
		bind(DbStarter.class).to(MongoDefaultStarterImpl.class).asEagerSingleton();
		bind(DaoConfig.class).toProvider(DaoConfigProvider.class).in(Singleton.class);
		bind(EntityCollectionManager.class).in(Singleton.class);
		bindConstant().annotatedWith(Names.named("default_db")).to("test_project_db");
		install(new MongoDaoModule());
	}
}