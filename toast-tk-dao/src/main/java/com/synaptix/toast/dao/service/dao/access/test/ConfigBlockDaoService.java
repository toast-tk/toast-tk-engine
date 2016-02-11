package com.synaptix.toast.dao.service.dao.access.test;

import javax.annotation.Nullable;

import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.mongodb.WriteConcern;
import com.synaptix.toast.dao.domain.impl.test.block.ConfigBlock;
import com.synaptix.toast.dao.service.dao.common.AbstractMongoDaoService;
import com.synaptix.toast.dao.service.dao.common.CommonMongoDaoService;
import com.synaptix.toast.dao.service.init.DbStarter;

public class ConfigBlockDaoService extends AbstractMongoDaoService<ConfigBlock> {

	public interface Factory {
		ConfigBlockDaoService create(final @Nullable @Assisted String dbName);
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