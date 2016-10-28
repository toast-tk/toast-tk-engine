package io.toast.tk.dao.service.dao.access.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import io.toast.tk.dao.domain.impl.common.IServiceFactory;
import io.toast.tk.dao.domain.impl.repository.ProjectImpl;
import io.toast.tk.dao.domain.impl.team.UserImpl;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;
import io.toast.tk.dao.service.init.DbStarter;

public class ProjectDaoService extends AbstractMongoDaoService<ProjectImpl> {
	
	public interface Factory extends IServiceFactory<ProjectDaoService>{
	}

	@Inject
	public ProjectDaoService(
		final DbStarter starter,
		final CommonMongoDaoService commonService,
		@Named("default_db") final String defaultDb,
		@Nullable @Assisted final String databaseName
	) {
		super(ProjectImpl.class, starter.getDatabaseByName(databaseName != null ? databaseName : defaultDb), commonService);
	}

	public ProjectImpl findProject(String idProject) {
		final Query<ProjectImpl> query = createQuery();
		query.field("_id").equal(new ObjectId(idProject));
		return findOne(query);
	}
	
	public ProjectImpl findProjectByProjectName(String projectName) {
		final Query<ProjectImpl> query = createQuery();
		query.field("name").equal(projectName);
		return findOne(query);
	}
	
	public List<ProjectImpl> findAllProjects() {
		final Query<ProjectImpl> query = createQuery();
		Iterable<ProjectImpl> fetch = query.fetch();
		Iterator<ProjectImpl> iterator = fetch.iterator();
		List<ProjectImpl> result = null;
		while(iterator.hasNext()){
			ProjectImpl project = iterator.next();
			if(result==null){
				result = new ArrayList<>();
			}
			result.add(project);
		}
		return result;
	}
}