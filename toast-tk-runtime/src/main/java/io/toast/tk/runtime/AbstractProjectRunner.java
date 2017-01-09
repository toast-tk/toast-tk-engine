package io.toast.tk.runtime;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Module;

import io.toast.tk.core.rest.RestUtils;
import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.IProject;
import io.toast.tk.dao.domain.impl.test.block.ITestPlan;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.runtime.dao.DAOManager;
import io.toast.tk.runtime.parse.TestParser;
import io.toast.tk.runtime.report.IHTMLReportGenerator;
import io.toast.tk.runtime.report.IProjectHtmlReportGenerator;

public abstract class AbstractProjectRunner extends AbstractRunner {

	private static final Logger LOG = LogManager.getLogger(AbstractProjectRunner.class);

	private final IHTMLReportGenerator htmlReportGenerator;

	private final IProjectHtmlReportGenerator projectHtmlReportGenerator;

	private String mongoDbHost;

	private int mongoDbPort;

	private String db;

	protected AbstractProjectRunner() {
		super();
		this.projectHtmlReportGenerator = injector.getInstance(IProjectHtmlReportGenerator.class);
		this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
	}

	protected AbstractProjectRunner(final Module extraModule, final String host, final int port, final String db) {
		this(extraModule);
		this.mongoDbHost = host;
		this.mongoDbPort = port;
		this.db = db;
	}

	protected AbstractProjectRunner(final String host, final int port, final String db) {
		this();
		this.mongoDbHost = host;
		this.mongoDbPort = port;
		this.db = db;
	}

	public AbstractProjectRunner(final Module... extraModules) {
		super(extraModules);
		this.projectHtmlReportGenerator = injector.getInstance(IProjectHtmlReportGenerator.class);
		this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
	}

	public final void test(ITestPlan project, boolean useRemoteRepository) throws Exception {
		execute(project, useRemoteRepository);
	}

	public final void test(final String name, final String idProject, final boolean useRemoteRepository) throws Exception {
		DAOManager.init(this.mongoDbHost, this.mongoDbPort, this.db);
		final TestPlanImpl lastExecution = DAOManager.getLastTestPlanExecution(name, idProject);
		final TestPlanImpl testPlanTemplate = DAOManager.getTestPlanTemplate(name, idProject);
		if (testPlanTemplate == null) {
			throw new IllegalAccessException("No reference test plan template found for: " + name);
		}
		updateTestPlanFromPreviousRun((ITestPlan)testPlanTemplate, lastExecution);
		execute(testPlanTemplate, useRemoteRepository);
		DAOManager.saveTestPlan(testPlanTemplate);
	}

	public final void testAndStore(String apiKey, final ITestPlan project) throws Exception {
		testAndStore(apiKey, project, false);
	}

	public final void testAndStore(String apiKey, final ITestPlan testPlan, final boolean useRemoteRepository) throws Exception {
		DAOManager.init(this.mongoDbHost, this.mongoDbPort, this.db);
		IProject project = DAOManager.getProjectByApiKey(apiKey);
		testPlan.setProject(project);
		final TestPlanImpl testPlanTemplate = DAOManager.getTestPlanTemplate(testPlan.getName(), project.getIdAsString());
		if(Objects.nonNull(testPlanTemplate)){
			DAOManager.updateTemplateFromTestPlan(testPlan);
		}else{
			DAOManager.saveTemplate((TestPlanImpl) testPlan);
		}
		final TestPlanImpl lastExecution = DAOManager.getLastTestPlanExecution(testPlan.getName(), project.getIdAsString());
		updateTestPlanFromPreviousRun(testPlan, lastExecution);
		execute(testPlan, useRemoteRepository); 
		DAOManager.saveTestPlan((TestPlanImpl)testPlan);
	}

	/**
	 * Update testPlan campaign test pages' previous execution status with previousRun execution time and success value
	 * 
	 * @param testPlan
	 * @param previousRun
	 */
	private void updateTestPlanFromPreviousRun(final ITestPlan testPlan, final TestPlanImpl previousRun) {
		testPlan.setId(null);
		testPlan.setIteration(previousRun.getIteration());
		for (final ICampaign newCampaign : testPlan.getCampaigns()) {
			for (final ITestPage newExecPage : newCampaign.getTestCases()) {
				updateTestPageFromPreviousRun( newCampaign, newExecPage, previousRun);
			}
		}
	}
	
	private void updateTestPageFromPreviousRun(final ICampaign newCampaign, final ITestPage newExecPage, final TestPlanImpl previousRun) {
		for (final ICampaign previousCampaign : previousRun.getCampaigns()) {
			if (newCampaign.getName().equals(previousCampaign.getName())) {
				for (ITestPage previousExecPage : previousCampaign.getTestCases()) {
					if (newExecPage.getName().equals(previousExecPage.getName())) {
						newExecPage.setPreviousIsSuccess(previousExecPage.isSuccess());
						newExecPage.setPreviousExecutionTime(previousExecPage.getExecutionTime());
					}
				}
			}
		}
	}
	

	public void execute(final ITestPlan testPlan, final boolean presetRepoFromWebApp) throws Exception {
		final TestRunner runner = injector.getInstance(TestRunner.class);
		if (presetRepoFromWebApp) {
			LOG.debug("Preset repository from webapp rest api...");
			final String repoWiki = RestUtils.downloadRepositoryAsWiki();
			final TestParser parser = new TestParser();
			final ITestPage repoAsTestPageForConvenience = parser.readString(repoWiki, null);
			runner.run(repoAsTestPageForConvenience);
		}
		execute(testPlan, runner);
	}

	private void execute(final ITestPlan testPlan, final TestRunner runner) {
		initEnvironment();
		for (final ICampaign campaign : testPlan.getCampaigns()) {
			for (ITestPage testPage : campaign.getTestCases()) {
				try {
					beginTest();
					testPage = runner.run(testPage);
					endTest();
				} catch (final Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		createAndOpenReport(testPlan);
		tearDownEnvironment();
	}

	protected void createAndOpenReport(final ITestPlan testPlan) {
		final String path = getReportsFolderPath();
		final String pageName = "Project_report";

		for (final ICampaign campaign : testPlan.getCampaigns()) {
			for (final ITestPage testPage : campaign.getTestCases()) {
				String testPageHtmlReport = htmlReportGenerator.generatePageHtml(testPage);
				htmlReportGenerator.writeFile(testPageHtmlReport, testPage.getName(), path);
			}
		}

		final String generatePageHtml = projectHtmlReportGenerator.generateProjectReportHtml(testPlan);
		this.projectHtmlReportGenerator.writeFile(generatePageHtml, pageName, path);
		openReport(path, pageName);
	}
}