package io.toast.tk.dao.service.dao.access.team;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import io.toast.tk.dao.domain.impl.team.GroupImpl;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;
import io.toast.tk.dao.service.init.DbStarter;

public class GroupDaoService extends AbstractMongoDaoService<GroupImpl> {

	public interface Factory {
		GroupDaoService create(final @Assisted String dbName);
	}

	@Inject
	public GroupDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		final @Named("default_db") String default_db,
		final @Assisted String dbName
	) {
		super(GroupImpl.class, starter.getDatabaseByName(dbName != null ? dbName : default_db), cService);
	}
}