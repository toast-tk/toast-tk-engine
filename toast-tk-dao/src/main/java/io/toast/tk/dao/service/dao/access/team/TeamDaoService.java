package io.toast.tk.dao.service.dao.access.team;

import java.util.Arrays;
import java.util.List;

import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import io.toast.tk.dao.domain.impl.common.IServiceFactory;
import io.toast.tk.dao.domain.impl.team.TeamImpl;
import io.toast.tk.dao.domain.impl.team.UserImpl;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;
import io.toast.tk.dao.service.init.DbStarter;

public class TeamDaoService extends AbstractMongoDaoService<TeamImpl> {

	public interface Factory extends IServiceFactory<TeamDaoService> {
	}

	@Inject
	public TeamDaoService(final DbStarter starter, 
			final CommonMongoDaoService commonService,
			@Named("default_db") final String defaultDb, 
			@Assisted final String databaseName) {
		super(TeamImpl.class, starter.getDatabaseByName(databaseName != null ? databaseName : defaultDb),
				commonService);
	}

	public List<TeamImpl> getUserTeams(UserImpl user) {
		Query<TeamImpl> query = createQuery();
		query.field("users").hasAnyOf(Arrays.asList(user));
		return query.asList();
	}
}