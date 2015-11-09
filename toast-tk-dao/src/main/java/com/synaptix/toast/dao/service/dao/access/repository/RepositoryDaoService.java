package com.synaptix.toast.dao.service.dao.access.repository;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.query.Query;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.mongodb.WriteConcern;
import com.synaptix.toast.dao.domain.impl.repository.RepositoryImpl;
import com.synaptix.toast.dao.service.dao.common.AbstractMongoDaoService;
import com.synaptix.toast.dao.service.dao.common.CommonMongoDaoService;
import com.synaptix.toast.dao.service.init.DbStarter;

public class RepositoryDaoService extends AbstractMongoDaoService<RepositoryImpl> {

	public interface Factory {

		RepositoryDaoService create(
			@Nullable @Assisted String dbName);
	}

	private static final Logger LOG = LogManager.getLogger(RepositoryDaoService.class);

	static final String CONTAINER_TYPE = "swing page";

	@Inject
	public RepositoryDaoService(
		DbStarter starter,
		CommonMongoDaoService cService,
		@Named("default_db") String default_db,
		@Nullable @Assisted String dbName) {
		super(RepositoryImpl.class, starter.getDatabaseByName(dbName != null ? dbName : default_db), cService);
	}

	public String getRepoAsJson() {
		Gson gSon = new Gson();
		Query<RepositoryImpl> query = createQuery();
		query.field("type").equal(CONTAINER_TYPE);
		List<RepositoryImpl> asList = query.asList();
		return gSon.toJson(asList);
	}

	@Deprecated
	public boolean saveRepoAsJson(
		String jsonRepo) {
		GsonBuilder gson = new GsonBuilder();
		Type typeOfT = new TypeToken<Collection<RepositoryImpl>>() {
		}.getType();
		try {
			gson.registerTypeHierarchyAdapter(ObjectId.class, new com.google.gson.JsonDeserializer<ObjectId>() {

				@Override
				public ObjectId deserialize(
					JsonElement json,
					Type typeOfT,
					JsonDeserializationContext context)
					throws JsonParseException {
					if(json == null) {
						return null;
					}
					return new ObjectId(json.toString());
				}
			});
			Collection<RepositoryImpl> repository = (Collection<RepositoryImpl>) gson.create().fromJson(
				jsonRepo,
				typeOfT);
			for(RepositoryImpl r : repository) {
				save(r, WriteConcern.ACKNOWLEDGED);
			}
			return true;
		}
		catch(Exception e) {
			LOG.error("Couldn't save json representation to mongo instance", e);
		}
		return false;
	}
}
