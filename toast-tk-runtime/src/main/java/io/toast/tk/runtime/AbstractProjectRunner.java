package io.toast.tk.runtime;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Module;

import io.toast.tk.core.rest.RestUtils;
import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.domain.impl.test.block.ICampaign;
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

	protected AbstractProjectRunner() {
		super();
		this.projectHtmlReportGenerator = injector.getInstance(IProjectHtmlReportGenerator.class);
		this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
	}

	protected AbstractProjectRunner(final Module extraModule, final String host, final int port) {
		this(extraModule);
		this.mongoDbHost = host;
		this.mongoDbPort = port;
	}

	protected AbstractProjectRunner(final String host, final int port) {
		this();
		this.mongoDbHost = host;
		this.mongoDbPort = port;
	}

	public AbstractProjectRunner(final Module extraModule) {
		super(extraModule);
		this.projectHtmlReportGenerator = injector.getInstance(IProjectHtmlReportGenerator.class);
		this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
	}

	public final void test(ITestPlan project, boolean useRemoteRepository) throws Exception {
		execute(project, useRemoteRepository);
	}

	public final void test(final String name, final boolean useRemoteRepository) throws Exception {
		DAOManager.init(this.mongoDbHost, this.mongoDbPort);
		final TestPlanImpl lastExecution = DAOManager.getLastTestPlanExecution(name);
		final TestPlanImpl testPlanTemplate = DAOManager.getTestPlanTemplate(name);
		if (testPlanTemplate == null) {
			throw new IllegalAccessException("No reference test plan template found for: " + name);
		}
		updateTestPlanFromPreviousRun((ITestPlan)testPlanTemplate, lastExecution);
		execute(testPlanTemplate, useRemoteRepository);
		DAOManager.saveTestPlan(testPlanTemplate);
	}

	public final void testAndStore(final ITestPlan project) throws Exception {
		testAndStore(project, false);
	}

	public final void testAndStore(final ITestPlan testPlan, final boolean useRemoteRepository) throws Exception {
		DAOManager.init(this.mongoDbHost, this.mongoDbPort);
		final TestPlanImpl testPlanTemplate = DAOManager.getTestPlanTemplate(testPlan.getName());
		if(Objects.nonNull(testPlanTemplate)){
			DAOManager.updateTemplateFromTestPlan(testPlan);
		}else{
			DAOManager.saveTemplate((TestPlanImpl) testPlan);
		}
		final TestPlanImpl lastExecution = DAOManager.getLastTestPlanExecution(testPlan.getName());
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
			for (final ICampaign previousCampaign : previousRun.getCampaigns()) {
				if (newCampaign.getName().equals(previousCampaign.getName())) {
					for (final ITestPage newExecPage : newCampaign.getTestCases()) {
						for (ITestPage previousExecPage : previousCampaign.getTestCases()) {
							if (newExecPage.getName().equals(previousExecPage.getName())) {
								newExecPage.setPreviousIsSuccess(previousExecPage.isSuccess());
								newExecPage.setPreviousExecutionTime(previousExecPage.getExecutionTime());
							}
						}
					}
				}
			}
		}
	}

	public void execute(final ITestPlan project, final boolean presetRepoFromWebApp) throws Exception {
		final TestRunner runner = injector.getInstance(TestRunner.class);
		if (presetRepoFromWebApp) {
			LOG.debug("Preset repository from webapp rest api...");
			final String repoWiki = RestUtils.downloadRepositoryAsWiki();
			final TestParser parser = new TestParser();
			final ITestPage repoAsTestPageForConvenience = parser.readString(repoWiki, null);
			runner.run(repoAsTestPageForConvenience);
		}
		execute(project, runner);
	}

	private void execute(final ITestPlan project, final TestRunner runner) {
		initEnvironment();
		for (final ICampaign campaign : project.getCampaigns()) {
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
		createAndOpenReport(project);
		tearDownEnvironment();
	}

	protected void createAndOpenReport(final ITestPlan project) {
		final String path = getReportsFolderPath();
		final String pageName = "Project_report";

		for (final ICampaign campaign : project.getCampaigns()) {
			for (final ITestPage testPage : campaign.getTestCases()) {
				String testPageHtmlReport = htmlReportGenerator.generatePageHtml(testPage);
				htmlReportGenerator.writeFile(testPageHtmlReport, testPage.getName(), path);
			}
		}

		final String generatePageHtml = projectHtmlReportGenerator.generateProjectReportHtml(project);
		this.projectHtmlReportGenerator.writeFile(generatePageHtml, pageName, path);
		openReport(path, pageName);
	}
}