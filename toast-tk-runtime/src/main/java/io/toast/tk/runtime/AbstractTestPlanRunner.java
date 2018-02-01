package io.toast.tk.runtime;

import com.google.inject.Module;
import io.toast.tk.core.rest.RestUtils;
import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.IProject;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.ITestPlan;
import io.toast.tk.runtime.dao.DAOManager;
import io.toast.tk.runtime.parse.TestParser;
import io.toast.tk.runtime.report.IHTMLReportGenerator;
import io.toast.tk.runtime.report.IProjectHtmlReportGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public abstract class AbstractTestPlanRunner extends AbstractRunner {

	private static final Logger LOG = LogManager.getLogger(AbstractTestPlanRunner.class);

	private final IHTMLReportGenerator htmlReportGenerator;

	private final IProjectHtmlReportGenerator projectHtmlReportGenerator;

	private String mongoDbHost;

	private int mongoDbPort;

	private String db;

	protected AbstractTestPlanRunner() {
		super();
		this.projectHtmlReportGenerator = injector.getInstance(IProjectHtmlReportGenerator.class);
		this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
	}

	protected AbstractTestPlanRunner(final String host, final int port, final String db, final Module... extraModule) {
		this(extraModule);
		this.mongoDbHost = host;
		this.mongoDbPort = port;
		this.db = db;
	}

	protected AbstractTestPlanRunner(final String host, final int port, final String db) {
		this();
		this.mongoDbHost = host;
		this.mongoDbPort = port;
		this.db = db;
	}

	public AbstractTestPlanRunner(final Module... extraModules) {
		super(extraModules);
		this.projectHtmlReportGenerator = injector.getInstance(IProjectHtmlReportGenerator.class);
		this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
	}

	public final void test(ITestPlan testplan, boolean useRemoteRepository, String apiKey) throws IOException {
		execute(testplan, useRemoteRepository, apiKey);
	}

	public final void test(final String name, final String idProject, final boolean useRemoteRepository, String apiKey) throws ToastRuntimeException {
		DAOManager.init(this.mongoDbHost, this.mongoDbPort, this.db);
		final TestPlanImpl lastExecution = DAOManager.getLastTestPlanExecution(name, idProject);
		final TestPlanImpl testPlanTemplate = DAOManager.getTestPlanTemplate(name, idProject);
		if (testPlanTemplate == null) {
			throw new ToastRuntimeException("No reference test plan template found for: " + name);
		}
		updateTestPlanFromPreviousRun((ITestPlan)testPlanTemplate, lastExecution);
		runAndSave(testPlanTemplate, useRemoteRepository, apiKey);
	}

	private void runAndSave(ITestPlan testPlan, boolean useRemoteRepository, String apiKey) throws ToastRuntimeException {
		try {
			execute(testPlan, useRemoteRepository, apiKey);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			throw new ToastRuntimeException("Error: Saving TestPlan template failed.");
		}
		try {
			DAOManager.saveTestPlan((TestPlanImpl)testPlan);
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
			throw new ToastRuntimeException("Error: Saving TestPlan execution report failed.");
		}
	}

	public final void testAndStore(String apiKey, final ITestPlan testplan) throws ToastRuntimeException {
		testAndStore(apiKey, testplan, false);
	}

	public final void testAndStore(String apiKey, final ITestPlan testPlan, final boolean useRemoteRepository) throws ToastRuntimeException {
		DAOManager.init(this.mongoDbHost, this.mongoDbPort, this.db);
		IProject project = DAOManager.getProjectByApiKey(apiKey);
		testPlan.setProject(project);
		final TestPlanImpl testPlanTemplate = DAOManager.getTestPlanTemplate(testPlan.getName(), project.getIdAsString());
		if(Objects.nonNull(testPlanTemplate)){
			try {
				DAOManager.updateTemplateFromTestPlan(testPlan);
			} catch (IllegalAccessException e) {
				LOG.error(e.getMessage(), e);
				throw new ToastRuntimeException("Error: Updating Test Plan template failed.");
			}
		}else{
			try {
				DAOManager.saveTemplate((TestPlanImpl) testPlan);
			} catch (IllegalAccessException e) {
				LOG.error(e.getMessage(), e);
				throw new ToastRuntimeException("Error: Saving TestPlan template failed.");
			}
		}
		final TestPlanImpl lastExecution = DAOManager.getLastTestPlanExecution(testPlan.getName(), project.getIdAsString());
		updateTestPlanFromPreviousRun(testPlan, lastExecution);
		runAndSave(testPlan, useRemoteRepository, apiKey);
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
				compareAndUpdateCampain(newCampaign, previousCampaign);
			}
		}
	}
	

	private void compareAndUpdateCampain(ICampaign newCampaign, ICampaign previousCampaign) {
		if (newCampaign.getName().equals(previousCampaign.getName())) {
            for (final ITestPage newExecPage : newCampaign.getTestCases()) {
                for (ITestPage previousExecPage : previousCampaign.getTestCases()) {
					compareAndUpdateTestPage(newExecPage, previousExecPage);
				}
            }
        }
	}

	private void compareAndUpdateTestPage(ITestPage newExecPage, ITestPage previousExecPage) {
		if (newExecPage.getName().equals(previousExecPage.getName())) {
            newExecPage.setPreviousIsSuccess(previousExecPage.isSuccess());
            newExecPage.setPreviousExecutionTime(previousExecPage.getExecutionTime());
        }
	}

	public void execute(final ITestPlan testPlan, final boolean presetRepoFromWebApp, final String apiKey) throws IOException {
		final TestRunner runner = injector.getInstance(TestRunner.class);
		if (presetRepoFromWebApp) {
			LOG.debug("Preset repository from webapp rest api...");
			final String repoWiki = RestUtils.downloadRepositoryAsWiki(apiKey);
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
					runner.run(testPage);
					endTest();
				} catch (final Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		if (shouldSendMail()) {
			mailReportSender.sendMailReport(testPlan);
		}
		createAndOpenReport(testPlan);
		tearDownEnvironment();
	}

	private void createAndOpenReport(final ITestPlan testPlan) {
		final String path = getReportsFolderPath();
		final String pageName = "testplan_report";

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
