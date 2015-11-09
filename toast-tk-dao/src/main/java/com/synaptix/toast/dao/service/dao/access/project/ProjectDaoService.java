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
import com.synaptix.toast.dao.domain.impl.test.block.ICampaign;
import com.synaptix.toast.dao.service.dao.common.AbstractMongoDaoService;
import com.synaptix.toast.dao.service.dao.common.CommonMongoDaoService;
import com.synaptix.toast.dao.service.init.DbStarter;

public class ProjectDaoService extends AbstractMongoDaoService<Project> {

	public interface Factory {
		ProjectDaoService create(
			@Assisted String dbName);
	}

	private final CampaignDaoService cDaoService;

	@Inject
	public ProjectDaoService(
		DbStarter starter,
		CommonMongoDaoService cService,
		@Assisted String dbName,
		@Named("default_db") String default_db,
		CampaignDaoService.Factory cDaoServiceFactory) {
		super(Project.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
		this.cDaoService = cDaoServiceFactory.create(dbName);
	}

	public Project getByName(
		String name) {
		Query<Project> query = createQuery();
		query.field("name").equal(name);
		return query.get();
	}

	public List<Project> getProjectHistory(
		Project project) {
		Query<Project> query = createQuery();
		Criteria nameCriteria = query.criteria("name").equal(project.getName());
		Criteria versionCriteria = query.criteria("version").equal(project.getVersion());
		Criteria iterationCriteria = query.criteria("iteration").lessThan(project.getIteration());
		query.and(nameCriteria, versionCriteria, iterationCriteria);
		List<Project> projectHistory = find(query).asList();
		Collections.sort(projectHistory, new Comparator<Project>() {

			@Override
			public int compare(
				Project o1,
				Project o2) {
				return o1.getIteration() - o2.getIteration();
			}
		});
		return projectHistory;
	}

	public Key<Project> saveReferenceProject(
		Project project) {
		short iteration = 0;
		project.setLast(false);
		project.setIteration(iteration);
		for(ICampaign c : project.getCampaigns()) {
			cDaoService.saveReference((Campaign) c);
		}
		return save(project);
	}
	
	public Key<Project> saveNewIteration(
		Project newEntry) {
		// update previous entry
		Project previousEntry = getLastByName(newEntry.getName());
		if(previousEntry != null) {
			previousEntry.setLast(false);
			newEntry.setIteration((short) (previousEntry.getIteration() + 1));
			save(previousEntry);
		}
		newEntry.setId(null);
		newEntry.setLast(true);
		for(ICampaign c : newEntry.getCampaigns()) {
			cDaoService.saveAsNewIteration((Campaign) c);
		}
		return save(newEntry);
	}
	public List<Project> findAllReferenceProjects() {
		Query<Project> query = createQuery();
		query.criteria("iteration").equal((short)0);
		return query.asList();
	}

	public List<Project> findAllLastProjects() {
		Query<Project> query = createQuery();
		query.field("last").equal(true);
		return query.asList();
	}

	public List<Project> findAllIterationsByProjectName(
		String pName,
		String version) {
		Query<Project> query = createQuery();
		CriteriaContainerImpl equal2 = query.criteria("version").equal(version);
		query.criteria("name").equal(pName).and(equal2);
		return find(query).asList();
	}

	public Project getLastByName(
		String name) {
		Query<Project> query = createQuery();
		query.field("name").equal(name).order("-iteration");
		return find(query).get();
	}

	public Project getByNameAndIteration(
		String pName,
		String iter) {
		Query<Project> query = createQuery();
		Criteria nameCriteria = query.criteria("name").equal(pName);
		Criteria iterationCriteria = query.criteria("iteration").equal(Short.valueOf(iter));
		query.and(nameCriteria, iterationCriteria);
		return find(query).get();
	}

	public Project getReferenceProjectByName(
		String projectName) {
		Query<Project> query = createQuery();
		Criteria nameCriteria = query.criteria("name").equal(projectName);
		Criteria iterationCriteria = query.criteria("iteration").equal((short)0);
		query.and(nameCriteria, iterationCriteria);
		return find(query).get();
	}
}
