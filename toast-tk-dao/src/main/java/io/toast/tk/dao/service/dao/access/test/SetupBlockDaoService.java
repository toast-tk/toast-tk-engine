package io.toast.tk.dao.service.dao.access.test;

import javax.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import io.toast.tk.dao.domain.impl.common.IServiceFactory;
import io.toast.tk.dao.domain.impl.test.block.SetupBlock;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;
import io.toast.tk.dao.service.init.DbStarter;

public class SetupBlockDaoService extends AbstractMongoDaoService<SetupBlock> {

	public interface Factory extends IServiceFactory<SetupBlockDaoService>{
	}

	@Inject
	public SetupBlockDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		final @Nullable @Assisted String dbName,
		final @Named("default_db") String default_db
	) {
		super(SetupBlock.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
	}
}