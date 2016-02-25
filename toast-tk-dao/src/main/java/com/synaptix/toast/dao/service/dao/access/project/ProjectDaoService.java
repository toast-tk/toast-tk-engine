package com.synaptix.toast.dao.service.dao.access.project;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.query.Criteria;
import com.github.jmkgreen.morphia.query.CriteriaContainerImpl;
import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.synaptix.toast.dao.domain.impl.report.Campaign;
import com.synaptix.toast.dao.domain.impl.report.Project;
import com.synaptix.toast.dao.service.dao.common.AbstractMongoDaoService;
import com.synaptix.toast.dao.service.dao.common.CommonMongoDaoService;
import com.synaptix.toast.dao.service.init.DbStarter;

public class ProjectDaoService extends AbstractMongoDaoService<Project> {

	public interface Factory {
		
		ProjectDaoService create(final @Assisted String dbName);
	}

	private final CampaignDaoService cDaoService;

	@Inject
	public ProjectDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		final @Assisted String dbName,
		final @Named("default_db") String default_db,
		final CampaignDaoService.Factory cDaoServiceFactory
	) {
		super(Project.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
		this.cDaoService = cDaoServiceFactory.create(dbName);
	}

	public Project getByName(final String name) {
		final Query<Project> query = createQuery();
		query.field("name").equal(name);
		return query.get();
	}

	private static class ProjectComparator implements Comparator<Project> {

		static final Comparator<Project> INSTANCE = new ProjectComparator();
		
		private ProjectComparator() {
			
		}
		
		@Override
		public int compare(final Project project1, final Project project2) {
			return project1.getIteration() - project2.getIteration();
		}
	}
	
	public List<Project> getProjectHistory(final Project project) {
		final Query<Project> query = createQuery();
		final Criteria nameCriteria = query.criteria("name").equal(project.getName());
		final Criteria versionCriteria = query.criteria("version").equal(project.getVersion());
		final Criteria iterationCriteria = query.criteria("iteration").lessThan(project.getIteration());
		query.and(nameCriteria, versionCriteria, iterationCriteria);
		final List<Project> projectHistory = find(query).asList();
		Collections.sort(projectHistory, ProjectComparator.INSTANCE);
		return projectHistory;
	}

	public Key<Project> saveReferenceProject(final Project project) {
		short iteration = 0;
		project.setLast(false);
		project.setIteration(iteration);
		project.getCampaigns().stream().forEach(c -> cDaoService.saveReference((Campaign) c));
		return save(project);
	}
	
	public Key<Project> saveNewIteration(final Project newEntry) {
		// update previous entry
		final Project previousEntry = getLastByName(newEntry.getName());
		if(previousEntry != null) {
			previousEntry.setLast(false);
			newEntry.setIteration((short) (previousEntry.getIteration() + 1));
			save(previousEntry);
		}
		newEntry.setId(null);
		newEntry.setLast(true);
		newEntry.getCampaigns().stream().forEach(c -> cDaoService.saveAsNewIteration((Campaign) c));
		return save(newEntry);
	}

	public List<Project> findAllReferenceProjects() {
		final Query<Project> query = createQuery();
		query.criteria("iteration").equal((short)0);
		return query.asList();
	}

	public List<Project> findAllLastProjects() {
		final Query<Project> query = createQuery();
		query.field("last").equal(true);
		return query.asList();
	}

	public List<Project> findAllIterationsByProjectName(
		final String pName,
		final String version
	) {
		final Query<Project> query = createQuery();
		final CriteriaContainerImpl equal2 = query.criteria("version").equal(version);
		query.criteria("name").equal(pName).and(equal2);
		return find(query).asList();
	}

	public Project getLastByName(
		final String name
	) {
		final Query<Project> query = createQuery();
		query.field("name").equal(name).order("-iteration");
		return find(query).get();
	}

	public Project getByNameAndIteration(
		final String pName,
		final String iter
	) {
		final Query<Project> query = createQuery();
		final Criteria nameCriteria = query.criteria("name").equal(pName);
		final Criteria iterationCriteria = query.criteria("iteration").equal(Short.valueOf(iter));
		query.and(nameCriteria, iterationCriteria);
		return find(query).get();
	}

	public Project getReferenceProjectByName(
		final String projectName
	) {
		final Query<Project> query = createQuery();
		final Criteria nameCriteria = query.criteria("name").equal(projectName);
		final Criteria iterationCriteria = query.criteria("iteration").equal((short)0);
		query.and(nameCriteria, iterationCriteria);
		return find(query).get();
	}
}