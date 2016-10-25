package io.toast.tk.dao.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;
import com.mongodb.MongoCredential;

import io.toast.tk.dao.config.DaoConfig;
import io.toast.tk.dao.config.DaoConfigProvider;
import io.toast.tk.dao.service.dao.common.EntityCollectionManager;
import io.toast.tk.dao.service.init.DbStarter;
import io.toast.tk.dao.service.init.MongoDefaultStarterImpl;

public class MongoModule extends AbstractModule {

	private final String mongoHost;

	private final int mongoPort;

	private MongoCredential credential;

	private String dbName;
	

	public MongoModule() {
		this.mongoHost = "localhost";
		this.mongoPort = 27017;
		this.dbName = null;
	}

	public MongoModule(final String mongoHost, 
			final int mongoPort, 
			final String dbName,
			MongoCredential credential) {
		this.mongoHost = mongoHost;
		this.mongoPort = mongoPort;
		this.dbName = dbName;
		this.credential = credential;
	}

	public MongoModule(final String mongoHost, final int mongoPort) {
		this.mongoHost = mongoHost;
		this.mongoPort = mongoPort;
		this.dbName = null;
		this.credential = null;
	}
	
	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("MongoHost")).to(mongoHost);
		bindConstant().annotatedWith(Names.named("MongoPort")).to(mongoPort);
		if(credential == null){
			bind(MongoCredential.class).toProvider(Providers.of(null));
		}else {
			bind(MongoCredential.class).toInstance(credential);
		}
		bind(DbStarter.class).to(MongoDefaultStarterImpl.class).asEagerSingleton();
		bind(DaoConfig.class).toProvider(DaoConfigProvider.class).in(Singleton.class);
		bind(EntityCollectionManager.class).in(Singleton.class);
		if(dbName == null){
			bindConstant().annotatedWith(Names.named("default_db")).to("test_project_db");
		}else {
			bindConstant().annotatedWith(Names.named("default_db")).to(dbName);
		}
		
		install(new MongoDaoModule());
	}

}