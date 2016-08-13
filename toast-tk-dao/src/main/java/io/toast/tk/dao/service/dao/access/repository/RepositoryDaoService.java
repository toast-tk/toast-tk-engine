package io.toast.tk.dao.service.dao.access.repository;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.Key;
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

import io.toast.tk.dao.domain.impl.common.IServiceFactory;
import io.toast.tk.dao.domain.impl.repository.RepositoryImpl;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;
import io.toast.tk.dao.service.init.DbStarter;

public class RepositoryDaoService extends AbstractMongoDaoService<RepositoryImpl> {

	public interface Factory extends IServiceFactory<RepositoryDaoService>{
	}

	private static final Logger LOG = LogManager.getLogger(RepositoryDaoService.class);

	@Deprecated
	static final String CONTAINER_TYPE = "swing page";

	private ElementDaoService eDaoService;

	@Inject
	public RepositoryDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		final @Named("default_db") String default_db,
		final @Nullable @Assisted String dbName,
		final ElementDaoService.Factory eDaoServiceFactory
	) {
		super(RepositoryImpl.class, starter.getDatabaseByName(dbName != null ? dbName : default_db), cService);
		this.eDaoService = eDaoServiceFactory.create(dbName);
	}

	@Deprecated
	public String getRepoAsJson() {
		final Gson gSon = new Gson();//Gson is immutable
		final Query<RepositoryImpl> query = createQuery();
		query.field("type").equal(CONTAINER_TYPE);
		final List<RepositoryImpl> asList = query.asList();
		return gSon.toJson(asList);
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	public boolean saveRepoAsJson(
		final String jsonRepo
	) {
		final GsonBuilder gson = new GsonBuilder();
		final Type typeOfT = new TypeToken<Collection<RepositoryImpl>>() {
									private static final long serialVersionUID = 1L;}
									.getType();
		try {
			gson.registerTypeHierarchyAdapter(ObjectId.class, new com.google.gson.JsonDeserializer<ObjectId>() {

				@Override
				public ObjectId deserialize(
					final JsonElement json,
					final Type typeOfT,
					final JsonDeserializationContext context
				) throws JsonParseException {
					if(json == null) {
						return null;
					}
					return new ObjectId(json.toString());
				}
			});
			final Collection<RepositoryImpl> repository = (Collection<RepositoryImpl>) gson.create().fromJson(
				jsonRepo,
				typeOfT
			);
			repository.stream().forEach(r -> save(r, WriteConcern.ACKNOWLEDGED));
			return true;
		}
		catch(final Exception e) {
			LOG.error("Couldn't save json representation to mongo instance", e);
		}
		return false;
	}

	@Override
	public Key<RepositoryImpl> save(RepositoryImpl entity) {
		entity.rows.stream().forEach(e -> eDaoService.save(e, WriteConcern.ACKNOWLEDGED));
		return super.save(entity);
	}

	@Override
	public Key<RepositoryImpl> save(RepositoryImpl entity, WriteConcern wc) {
		entity.rows.stream().forEach(e -> eDaoService.save(e, wc));
		return super.save(entity, wc);
	}
	
}