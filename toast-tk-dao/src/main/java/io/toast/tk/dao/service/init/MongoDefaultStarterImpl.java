package io.toast.tk.dao.service.init;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

import io.toast.tk.dao.config.DaoConfig;
import io.toast.tk.dao.domain.Domain;

public class MongoDefaultStarterImpl implements DbStarter {

	private static final Logger LOG = LogManager.getLogger(MongoDefaultStarterImpl.class);

	private Morphia morphia;

	private MongoClient mClient;

	private Map<String, Datastore> dsMap;


	private final String mongoHost;

	private final int mongoPort;

	@Inject
	public MongoDefaultStarterImpl(
		final DaoConfig config,
		final @Named("MongoHost") String mongoHost,
		final @Named("MongoPort") int mongoPort
	) {
		this.mongoHost = mongoHost == null ? config.getMongoServer() : mongoHost;
		this.mongoPort = mongoPort == -1 ? config.getMongoPort() : mongoPort;
		init();
	}

	private void init() {
		try {
			this.dsMap = new HashMap<>();
			this.mClient = new MongoClient(this.mongoHost, this.mongoPort);
			this.mClient.setWriteConcern(WriteConcern.JOURNALED);
			this.morphia = new Morphia();
			processMappings();
		}
		catch(final UnknownHostException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void processMappings() {
		final Reflections reflection = new Reflections(Domain.class.getPackage().getName());
		final Set<Class<?>> typesAnnotatedWith = reflection.getTypesAnnotatedWith(Entity.class);
		typesAnnotatedWith.stream().forEach(c -> processMapping(c));
	}

	private void processMapping(Class<?> c) {
		morphia.map(c);
		LOG.info("{} type has been registered to Morphia !", c);
	}

	@Override
	public Datastore getDatabaseByName(final String name) {
		if(dsMap.get(name) == null) {
			Datastore ds = morphia.createDatastore(mClient, name);
			ds.ensureCaps();
			ds.ensureIndexes();
			dsMap.put(name, ds);
		}
		return dsMap.get(name);
	}
}