package com.synaptix.toast.dao.service.dao.access.team;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.synaptix.toast.dao.domain.impl.team.UserImpl;
import com.synaptix.toast.dao.service.dao.common.AbstractMongoDaoService;
import com.synaptix.toast.dao.service.dao.common.CommonMongoDaoService;
import com.synaptix.toast.dao.service.init.DbStarter;

public class UserDaoService extends AbstractMongoDaoService<UserImpl> {

	public interface Factory {

		UserDaoService create(
			@Assisted String dbName);
	}

	@Inject
	public UserDaoService(
		DbStarter starter,
		CommonMongoDaoService cService,
		@Assisted String dbName) {
		super(UserImpl.class, starter.getDatabaseByName(dbName), cService);
	}
}
