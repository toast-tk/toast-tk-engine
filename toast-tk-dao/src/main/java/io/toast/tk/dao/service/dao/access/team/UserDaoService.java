package io.toast.tk.dao.service.dao.access.team;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import io.toast.tk.dao.domain.impl.common.IServiceFactory;
import io.toast.tk.dao.domain.impl.team.TeamImpl;
import io.toast.tk.dao.domain.impl.team.UserImpl;
import io.toast.tk.dao.domain.impl.test.block.IProject;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;
import io.toast.tk.dao.service.init.DbStarter;

public class UserDaoService extends AbstractMongoDaoService<UserImpl> {

	public interface Factory extends IServiceFactory<UserDaoService>{}

	private TeamDaoService teamService;
	
	@Inject
	public UserDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		final @Named("default_db") String default_db,
		final @Assisted String dbName,
		final TeamDaoService.Factory tDaoServiceFactory
	) {
		super(UserImpl.class, starter.getDatabaseByName(dbName != null ? dbName : default_db), cService);
		teamService = tDaoServiceFactory.create(dbName);
	}

	public List<IProject> getUserProjects(UserImpl user) {
		List<TeamImpl> teams = teamService.getUserTeams(user);
		List<IProject> result = new ArrayList<>();
		if(Objects.nonNull(teams)){
			teams.stream().forEach(t -> result.addAll(t.getProjects()));
		}
		return result;
	}
}