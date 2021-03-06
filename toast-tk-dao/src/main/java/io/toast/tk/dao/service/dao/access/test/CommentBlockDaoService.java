package io.toast.tk.dao.service.dao.access.test;

import javax.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import io.toast.tk.dao.domain.impl.common.IServiceFactory;
import io.toast.tk.dao.domain.impl.test.block.CommentBlock;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;
import io.toast.tk.dao.service.init.DbStarter;

public class CommentBlockDaoService extends AbstractMongoDaoService<CommentBlock> {

	public interface Factory extends IServiceFactory<CommentBlockDaoService>{
	}

	@Inject
	public CommentBlockDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		final @Nullable @Assisted String dbName,
		final @Named("default_db") String default_db
	) {
		super(CommentBlock.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
	}
}