package io.toast.tk.dao.service.dao.access.plan;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.query.Criteria;
import com.github.jmkgreen.morphia.query.CriteriaContainerImpl;
import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.mongodb.WriteConcern;

import io.toast.tk.dao.domain.impl.common.IServiceFactory;
import io.toast.tk.dao.domain.impl.report.Campaign;
import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.domain.impl.repository.ProjectImpl;
import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.ITestPlan;
import io.toast.tk.dao.service.dao.access.test.TestPageDaoService;
import io.toast.tk.dao.service.dao.common.AbstractMongoDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;
import io.toast.tk.dao.service.init.DbStarter;

public class TestPlanDaoService extends AbstractMongoDaoService<TestPlanImpl> {

	public interface Factory extends IServiceFactory<TestPlanDaoService> {
	}

	private static final Logger LOG = LogManager.getLogger(TestPlanDaoService.class);

	private final CampaignDaoService cDaoService;
	private final TestPageDaoService tDaoService;

	@Inject
	public TestPlanDaoService(final DbStarter starter, final CommonMongoDaoService commonService,
			@Assisted final String databaseName, @Named("default_db") final String defaultDb,
			final CampaignDaoService.Factory cDaoServiceFactory, final TestPageDaoService.Factory tDaoServiceFactory) {
		super(TestPlanImpl.class, starter.getDatabaseByName(databaseName == null ? defaultDb : databaseName),
				commonService);
		this.cDaoService = cDaoServiceFactory.create(databaseName);
		this.tDaoService = tDaoServiceFactory.create(databaseName);
	}

	public TestPlanImpl getByName(final String name, final String idProject) {
		final Query<TestPlanImpl> query = createQuery();
		final Criteria nameCriteria = query.criteria("name").equal(name);
		final Criteria idProjectCriteria = query.criteria("project._id").equal(new ObjectId(idProject));
		query.and(nameCriteria,idProjectCriteria);
		return find(query).get();
	}

	private static class TestPlanComparator implements Comparator<TestPlanImpl> {
		static final Comparator<TestPlanImpl> INSTANCE = new TestPlanComparator();

		private TestPlanComparator() {
		}

		@Override
		public int compare(final TestPlanImpl testPlan1, final TestPlanImpl testPlan2) {
			return testPlan1.getIteration() - testPlan2.getIteration();
		}
	}

	public List<TestPlanImpl> getProjectHistory(final TestPlanImpl testPlan) throws IllegalAccessException {
		checkIfHasProject(testPlan);
		final Query<TestPlanImpl> query = createQuery();
		final Criteria nameCriteria = query.criteria("name").equal(testPlan.getName());
		final Criteria versionCriteria = query.criteria("version").equal(testPlan.getVersion());
		final Criteria idProjectCriteria = query.criteria("project._id").equal(testPlan.getProject().getId());
		final Criteria iterationCriteria = query.criteria("iteration").lessThan(testPlan.getIteration());
		query.and(nameCriteria, versionCriteria, iterationCriteria, idProjectCriteria);
		final List<TestPlanImpl> projectHistory = find(query).asList();
		Collections.sort(projectHistory, TestPlanComparator.INSTANCE);
		return projectHistory;
	}
	
	public Key<TestPlanImpl> detachTemplate(final TestPlanImpl testPlan) throws IllegalAccessException {
		testPlan.setProject(null);
		return save(testPlan, WriteConcern.ACKNOWLEDGED);
	}

	public Key<TestPlanImpl> saveTemplate(final TestPlanImpl testPlan) throws IllegalAccessException {
		checkIfHasProject(testPlan);
		short iteration = 0;
		testPlan.setLast(false);
		testPlan.setIteration(iteration);
		if (Objects.nonNull(testPlan.getCampaigns())) {
			testPlan.getCampaigns().stream().forEach(c -> cDaoService.saveReference((Campaign) c));
		}
		return save(testPlan, WriteConcern.ACKNOWLEDGED);
	}

	private void checkIfHasProject(final TestPlanImpl testPlan) throws IllegalAccessException {
		if (testPlan.getProject() == null) {
			throw new IllegalAccessException("No project set in test plan, please assign one !");
		}
	}

	public Key<TestPlanImpl> saveNewIteration(final TestPlanImpl testPlan) throws IllegalAccessException {
		checkIfHasProject(testPlan);
		final TestPlanImpl previousEntry = getLastByName(testPlan.getName(), testPlan.getProject().getId().toString());
		if (previousEntry != null) {
			previousEntry.setLast(false);
			testPlan.setIteration((short) (previousEntry.getIteration() + 1));
			save(previousEntry);
		}
		testPlan.setId(null);
		testPlan.setLast(true);
		if (Objects.nonNull(testPlan.getCampaigns())) {
			testPlan.getCampaigns().stream().forEach(c -> cDaoService.saveAsNewIteration((Campaign) c));
		}

		return save(testPlan);
	}

	public List<TestPlanImpl> findAllReferenceProjects(String idProject) {
		final Query<TestPlanImpl> query = createQuery();
		query.criteria("project._id").equal(new ObjectId(idProject));
		query.criteria("iteration").equal((short) 0);
		return query.asList();
	}
	
	public TestPlanImpl findTestPlanById(String id) {
		final Query<TestPlanImpl> query = createQuery();
		query.criteria("_id").equal(new ObjectId(id));
		return findOne(query);
	}

	public List<TestPlanImpl> findAllLastProjects(String idProject) {
		final Query<TestPlanImpl> query = createQuery();
		query.criteria("project._id").equal(new ObjectId(idProject));
		query.field("last").equal(true);
		return find(query).asList();
	}

	public List<TestPlanImpl> findAllIterationsByProjectName(final String pName, final String idProject, final String version) {
		final Query<TestPlanImpl> query = createQuery();
		final CriteriaContainerImpl versionCriteria = query.criteria("version").equal(version);
		final CriteriaContainerImpl idProjectCriteria = query.criteria("project._id").equal(new ObjectId(idProject));
		query.criteria("name").equal(pName).and(versionCriteria, idProjectCriteria);
		return find(query).asList();
	}

	public TestPlanImpl getLastByName(final String name,  final String idProject) {
		final Query<TestPlanImpl> query = createQuery();
		query.field("name").equal(name).order("-iteration");
		query.criteria("project._id").equal(new ObjectId(idProject));
		return find(query).get();
	}

	public TestPlanImpl getByNameAndIteration(final String pName,  final String idProject, final String iter) {
		final Query<TestPlanImpl> query = createQuery();
		final Criteria nameCriteria = query.criteria("name").equal(pName);
		final CriteriaContainerImpl idProjectCriteria = query.criteria("project._id").equal(new ObjectId(idProject));
		final Criteria iterationCriteria = query.criteria("iteration").equal(Short.valueOf(iter));
		query.and(nameCriteria, iterationCriteria, idProjectCriteria);
		return find(query).get();
	}

	public TestPlanImpl getReferenceProjectByName(final String projectName, final String idProject) {
		final Query<TestPlanImpl> query = createQuery();
		final Criteria nameCriteria = query.criteria("name").equal(projectName);
		final Criteria iterationCriteria = query.criteria("iteration").equal((short) 0);
		query.and(nameCriteria, iterationCriteria);
		return find(query).get();
	}

	public ITestPlan updateTemplateFromTestPlan(final ITestPlan testPlan) throws IllegalAccessException {
		if (testPlan.getProject() == null) {
			throw new IllegalAccessException("No project set in test plan, please assign one !");
		}
		ProjectImpl projectImpl = (ProjectImpl)testPlan.getProject();
		
		final TestPlanImpl template = findTemplate(testPlan, projectImpl);
		
		update(testPlan, template);
		save((TestPlanImpl) testPlan);
		return testPlan;
	}

	private TestPlanImpl findTemplate(final ITestPlan testPlan, ProjectImpl projectImpl) throws IllegalAccessException {
		final TestPlanImpl template;
		if(testPlan.getIdAsString() != null){
			TestPlanImpl tPlan = findTestPlanById(testPlan.getIdAsString());

			if(tPlan!= null && tPlan.getIteration() != 0){
				template = getReferenceProjectByName(tPlan.getName(), projectImpl.getId().toString());
				if (template == null) {
					throw new IllegalAccessException("No template found for provided test plan: " + testPlan.getName());
				}
			}else {
				template = tPlan;
			}

		}else {
			template = getReferenceProjectByName(testPlan.getName(), projectImpl.getId().toString());
			if (template == null) {
				throw new IllegalAccessException("No template found for provided test plan: " + testPlan.getName());
			}			
		}
		return template;
	}

	private void update(ITestPlan testPlan, TestPlanImpl template) {
		testPlan.setId(template.getId().toString());
		if (Objects.isNull(testPlan.getCampaigns())) {
			return;
		}
		for (ICampaign testPlanCampaign : testPlan.getCampaigns()) {
			ICampaign campaignWithId = campaignWithId(template, testPlanCampaign.getName());
			if (Objects.nonNull(campaignWithId)) {
				testPlanCampaign.setId(new ObjectId(campaignWithId.getIdAsString()));
				if (Objects.nonNull(testPlanCampaign.getTestCases())) {
					updateTestScripts(testPlanCampaign, campaignWithId);
				}
			}
			cDaoService.saveReference((Campaign) testPlanCampaign);
		}
		
	}

	private void updateTestScripts(ICampaign testPlanCampaign, ICampaign campaignWithId) {
		for (ITestPage testPlanPage : testPlanCampaign.getTestCases()) {
			LOG.info("Looking for test page id -> " + testPlanPage.getIdAsString());
			LOG.info("Looking for test page name -> " + testPlanPage.getName());
			ITestPage testPageWithId = testPageWithId(campaignWithId, testPlanPage.getName());
			if (Objects.nonNull(testPageWithId)) {
				LOG.info("Found for test page id -> " + testPageWithId.getIdAsString());
				testPlanPage.setId(testPageWithId.getIdAsString());
			}
			// FIME: need to be added as a scenario in the
			// scenario collection ?
			try{
				tDaoService.saveReference(testPlanPage);
			}catch(Exception e){
				LOG.error("error while saving page with id -> " + testPlanPage.getIdAsString(),e);
				
			}
			
		}
	}

	private ICampaign campaignWithId(ITestPlan testPlan, String campaignName) {
		for (ICampaign campaign : testPlan.getCampaigns()) {
			if (campaign.getName().equalsIgnoreCase(campaignName.toLowerCase())) {
				return campaign;
			}
		}
		return null;
	}

	private ITestPage testPageWithId(ICampaign templateCampaign, String testPageName) {
		for (ITestPage test : templateCampaign.getTestCases()) {
			if (test.getName().equalsIgnoreCase(testPageName.toLowerCase())) {
				return test;
			}
		}
		return null;
	}
}