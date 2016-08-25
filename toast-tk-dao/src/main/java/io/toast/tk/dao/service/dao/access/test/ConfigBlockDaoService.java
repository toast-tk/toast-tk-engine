package io.toast.tk.dao.service.dao.access.test;

import javax.annotation.Nullable;

import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.mongodb.WriteConcern;

import io.toast.tk.dao.domain.impl.common.IServiceFactory;
import io.toast.tk.dao.domain.impl.test.block.ConfigBlock;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;
import io.toast.tk.dao.service.init.DbStarter;

public class ConfigBlockDaoService extends AbstractMongoDaoService<ConfigBlock> {

	
	public interface Factory extends IServiceFactory<ConfigBlockDaoService>{
	}

	ConfigBlock configBlock;

	@Inject
	public ConfigBlockDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		final @Nullable @Assisted String dbName,
		final @Named("default_db") String default_db
	) {
		super(ConfigBlock.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
	}

	public ConfigBlock loadConfigBlock(final String name) {
		final Query<ConfigBlock> query = createQuery();
		query.field("componentName").equal(name);
		return query.get();
	}

	public void saveNormal(final ConfigBlock configBlock) {
		save(configBlock, WriteConcern.NORMAL);
	}
}