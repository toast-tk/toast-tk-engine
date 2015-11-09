package com.synaptix.toast.dao.service.dao.access.test;

import javax.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.synaptix.toast.dao.domain.impl.test.block.CommentBlock;
import com.synaptix.toast.dao.service.dao.common.AbstractMongoDaoService;
import com.synaptix.toast.dao.service.dao.common.CommonMongoDaoService;
import com.synaptix.toast.dao.service.init.DbStarter;

public class CommentBlockDaoService extends AbstractMongoDaoService<CommentBlock> {

	public interface Factory {
		CommentBlockDaoService create(
			@Nullable @Assisted String dbName);
	}

	@Inject
	public CommentBlockDaoService(
		DbStarter starter,
		CommonMongoDaoService cService,
		@Nullable @Assisted String dbName,
		@Named("default_db") String default_db) {
		super(CommentBlock.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
	}
}
