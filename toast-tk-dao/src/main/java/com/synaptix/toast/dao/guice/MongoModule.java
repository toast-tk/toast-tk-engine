package com.synaptix.toast.dao.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.synaptix.toast.dao.config.Config;
import com.synaptix.toast.dao.config.ConfigProvider;
import com.synaptix.toast.dao.service.dao.common.EntityCollectionManager;
import com.synaptix.toast.dao.service.init.DbStarter;
import com.synaptix.toast.dao.service.init.MongoDefaultStarterImpl;

public class MongoModule extends AbstractModule {

	private final String mongoHost;

	private final int mongoPort;

	public MongoModule() {
		super();
		this.mongoHost = "";
		this.mongoPort = -1;
	}

	public MongoModule(
		String mongoHost,
		int mongoPort) {
		super();
		this.mongoHost = mongoHost;
		this.mongoPort = mongoPort;
	}

	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("MongoHost")).to(mongoHost);
		bindConstant().annotatedWith(Names.named("MongoPort")).to(mongoPort);
		bind(DbStarter.class).to(MongoDefaultStarterImpl.class).asEagerSingleton();
		bind(Config.class).toProvider(ConfigProvider.class).in(Singleton.class);
		bind(EntityCollectionManager.class).in(Singleton.class);
		bindConstant().annotatedWith(Names.named("default_db")).to("test_project_db");
		install(new MongoDaoModule());
	}
}
