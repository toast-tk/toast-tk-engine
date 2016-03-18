package com.synaptix.toast.dao.service.dao.access.team;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.synaptix.toast.dao.domain.impl.team.GroupImpl;
import com.synaptix.toast.dao.service.dao.common.AbstractMongoDaoService;
import com.synaptix.toast.dao.service.dao.common.CommonMongoDaoService;
import com.synaptix.toast.dao.service.init.DbStarter;

public class GroupDaoService extends AbstractMongoDaoService<GroupImpl> {

	public interface Factory {

		GroupDaoService create(final @Assisted String dbName);
	}

	@Inject
	public GroupDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		final @Assisted String dbName
	) {
		super(GroupImpl.class, starter.getDatabaseByName(dbName), cService);
	}
}