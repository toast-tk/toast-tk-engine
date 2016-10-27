package io.toast.tk.runtime.dao;

import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.MongoCredential;

import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.domain.impl.repository.ProjectImpl;
import io.toast.tk.dao.domain.impl.team.UserImpl;
import io.toast.tk.dao.domain.impl.test.block.ITestPlan;
import io.toast.tk.dao.guice.MongoModule;
import io.toast.tk.dao.service.dao.access.plan.TestPlanDaoService;
import io.toast.tk.dao.service.dao.access.repository.ProjectDaoService;
import io.toast.tk.dao.service.dao.access.team.UserDaoService;

public class DAOManager {

	private Injector mongoServiceInjector;

	private TestPlanDaoService.Factory testPlanFactory;

	private TestPlanDaoService testPlanService;

	private UserDaoService.Factory userFactory;

	private UserDaoService userService;
	
	private ProjectDaoService.Factory projectFactory;

	private ProjectDaoService projectService;

	private static DAOManager INSTANCE;

	private DAOManager(
		final String mongoHost, 
		final int mongoPort,
		final String mongoDb,
		final MongoCredential credential
	) {
		this.mongoServiceInjector = Guice.createInjector(new MongoModule(mongoHost, mongoPort, mongoDb, credential));
		this.testPlanFactory = mongoServiceInjector.getInstance(TestPlanDaoService.Factory.class);
		this.testPlanService = testPlanFactory.create(mongoDb);
		this.userFactory = mongoServiceInjector.getInstance(UserDaoService.Factory.class);
		this.userService = userFactory.create(mongoDb);
		this.projectFactory = mongoServiceInjector.getInstance(ProjectDaoService.Factory.class);
		this.projectService = projectFactory.create(mongoDb);
	}
	
	public static synchronized DAOManager init(
		final String mongoHost, 
		final int mongoPort
	) {
		return init(mongoHost, mongoPort, null, null);
	}
	
	public static synchronized DAOManager init(
			final String mongoHost, 
			final int mongoPort,
			final String mongoDb,
			final MongoCredential credential) {
		if(INSTANCE == null) {
			INSTANCE = new DAOManager(mongoHost, mongoPort, mongoDb, credential);
		}
		return INSTANCE;
	}
	
	private static synchronized DAOManager getInstance() {
		if(INSTANCE == null) {
			throw new RuntimeException("Mongo Host not provided !");
		}
		return INSTANCE;
	}

	TestPlanDaoService getTestPlanDaoService() {
		return testPlanService;
	}
	
	ProjectDaoService getProjectDaoService() {
		return projectService;
	}
	
	UserDaoService getUserDaoService() {
		return userService;
	}
	
	public static TestPlanImpl getLastTestPlanExecution(final String projectName, final String idProject) {
		return getInstance().getTestPlanDaoService().getLastByName(projectName, idProject);
	}

	public static TestPlanImpl getTestPlanTemplate(final String projectName, final String idProject) {
		return getInstance().getTestPlanDaoService().getReferenceProjectByName(projectName, idProject);
	}
	
	public static void saveTestPlan(final TestPlanImpl project) throws IllegalAccessException {
		getInstance().getTestPlanDaoService().saveNewIteration(project);
	}

	public static List<TestPlanImpl> getProjectHistory(final TestPlanImpl project) throws IllegalAccessException {
		return getInstance().getTestPlanDaoService().getProjectHistory(project);
	}

	public static void updateTemplateFromTestPlan(ITestPlan testPlan) throws IllegalAccessException {
		getInstance().getTestPlanDaoService().updateTemplateFromTestPlan(testPlan);
	}

	public static void saveTemplate(TestPlanImpl testPlan) throws IllegalAccessException {
		getInstance().getTestPlanDaoService().saveTemplate(testPlan);		
	}

	public static ProjectImpl getProjectByApiKey(String apiKey) {
		UserImpl user = getInstance().getUserDaoService().findUserByToken(apiKey);
		ProjectImpl project = getInstance().getProjectDaoService().findProject(user.getIdProject());
		return project;
	}
}