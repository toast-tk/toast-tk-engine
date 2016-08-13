package io.toast.tk.dao.service.dao.access.repository;

import javax.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import io.toast.tk.dao.domain.impl.repository.ProjectImpl;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;
import io.toast.tk.dao.service.init.DbStarter;

public class ProjectDaoService extends AbstractMongoDaoService<ProjectImpl> {
	
	public interface Factory {
		ProjectDaoService create(final @Nullable @Assisted String dbName);
	}

	@Inject
	public ProjectDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		final @Named("default_db") String default_db,
		final @Nullable @Assisted String dbName
	) {
		super(ProjectImpl.class, starter.getDatabaseByName(dbName != null ? dbName : default_db), cService);
	}

}