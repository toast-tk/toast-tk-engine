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
		ConfigBlockDaoService create(
			@Nullable @Assisted String dbName);
	}

	ConfigBlock configBlock;

	@Inject
	public ConfigBlockDaoService(
		DbStarter starter,
		CommonMongoDaoService cService,
		@Nullable @Assisted String dbName,
		@Named("default_db") String default_db) {
		super(ConfigBlock.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
	}

	public ConfigBlock loadConfigBlock(
		String name) {
		final Query<ConfigBlock> query = createQuery();
		query.field("componentName").equal(name);
		return query.get();
	}

	public void saveNormal(
		final ConfigBlock configBlock
		) {
		save(configBlock, WriteConcern.NORMAL);
	}
}