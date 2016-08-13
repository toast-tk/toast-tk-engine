package io.toast.tk.dao.service.dao.access.plan;

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

import io.toast.tk.dao.domain.impl.common.IServiceFactory;
import io.toast.tk.dao.domain.impl.report.Campaign;
import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;
import io.toast.tk.dao.service.init.DbStarter;

public class TestPlanDaoService extends AbstractMongoDaoService<TestPlanImpl> {

	public interface Factory extends IServiceFactory<TestPlanDaoService>{
	}

	private final CampaignDaoService cDaoService;

	@Inject
	public TestPlanDaoService(
		final DbStarter starter,
		final CommonMongoDaoService cService,
		final @Assisted String dbName,
		final @Named("default_db") String default_db,
		final CampaignDaoService.Factory cDaoServiceFactory
	) {
		super(TestPlanImpl.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
		this.cDaoService = cDaoServiceFactory.create(dbName);
	}

	public TestPlanImpl getByName(final String name) {
		final Query<TestPlanImpl> query = createQuery();
		query.field("name").equal(name);
		return query.get();
	}

	private static class ProjectComparator implements Comparator<TestPlanImpl> {

		static final Comparator<TestPlanImpl> INSTANCE = new ProjectComparator();
		
		private ProjectComparator() {
			
		}
		
		@Override
		public int compare(final TestPlanImpl project1, final TestPlanImpl project2) {
			return project1.getIteration() - project2.getIteration();
		}
	}
	
	public List<TestPlanImpl> getProjectHistory(final TestPlanImpl project) {
		final Query<TestPlanImpl> query = createQuery();
		final Criteria nameCriteria = query.criteria("name").equal(project.getName());
		final Criteria versionCriteria = query.criteria("version").equal(project.getVersion());
		final Criteria iterationCriteria = query.criteria("iteration").lessThan(project.getIteration());
		query.and(nameCriteria, versionCriteria, iterationCriteria);
		final List<TestPlanImpl> projectHistory = find(query).asList();
		Collections.sort(projectHistory, ProjectComparator.INSTANCE);
		return projectHistory;
	}

	public Key<TestPlanImpl> saveReferenceProject(final TestPlanImpl project) {
		short iteration = 0;
		project.setLast(false);
		project.setIteration(iteration);
		project.getCampaigns().stream().forEach(c -> cDaoService.saveReference((Campaign) c));
		return save(project);
	}
	
	public Key<TestPlanImpl> saveNewIteration(final TestPlanImpl newEntry) {
		// update previous entry
		final TestPlanImpl previousEntry = getLastByName(newEntry.getName());
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

	public List<TestPlanImpl> findAllReferenceProjects() {
		final Query<TestPlanImpl> query = createQuery();
		query.criteria("iteration").equal((short)0);
		return query.asList();
	}

	public List<TestPlanImpl> findAllLastProjects() {
		final Query<TestPlanImpl> query = createQuery();
		query.field("last").equal(true);
		return query.asList();
	}

	public List<TestPlanImpl> findAllIterationsByProjectName(
		final String pName,
		final String version
	) {
		final Query<TestPlanImpl> query = createQuery();
		final CriteriaContainerImpl equal2 = query.criteria("version").equal(version);
		query.criteria("name").equal(pName).and(equal2);
		return find(query).asList();
	}

	public TestPlanImpl getLastByName(
		final String name
	) {
		final Query<TestPlanImpl> query = createQuery();
		query.field("name").equal(name).order("-iteration");
		return find(query).get();
	}

	public TestPlanImpl getByNameAndIteration(
		final String pName,
		final String iter
	) {
		final Query<TestPlanImpl> query = createQuery();
		final Criteria nameCriteria = query.criteria("name").equal(pName);
		final Criteria iterationCriteria = query.criteria("iteration").equal(Short.valueOf(iter));
		query.and(nameCriteria, iterationCriteria);
		return find(query).get();
	}

	public TestPlanImpl getReferenceProjectByName(
		final String projectName
	) {
		final Query<TestPlanImpl> query = createQuery();
		final Criteria nameCriteria = query.criteria("name").equal(projectName);
		final Criteria iterationCriteria = query.criteria("iteration").equal((short)0);
		query.and(nameCriteria, iterationCriteria);
		return find(query).get();
	}
}