package com.synaptix.toast.dao.service.dao.access.test;

import javax.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.synaptix.toast.dao.domain.impl.test.block.InsertBlock;
import com.synaptix.toast.dao.service.dao.common.AbstractMongoDaoService;
import com.synaptix.toast.dao.service.dao.common.CommonMongoDaoService;
import com.synaptix.toast.dao.service.init.DbStarter;

public class InsertBlockDaoService extends AbstractMongoDaoService<InsertBlock> {

	public interface Factory {
		InsertBlockDaoService create(
			@Nullable @Assisted String dbName);
	}

	@Inject
	public InsertBlockDaoService(
		DbStarter starter,
		CommonMongoDaoService cService,
		@Nullable @Assisted String dbName,
		@Named("default_db") String default_db) {
		super(InsertBlock.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
	}
}
