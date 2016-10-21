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

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;
import java.util.Properties;

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

	protected AbstractTestPlanRunner(final Module extraModule, final String host, final int port, final String db) {
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

	public final void test(ITestPlan testplan, boolean useRemoteRepository) throws IOException {
		execute(testplan, useRemoteRepository);
	}

	public final void test(final String name, final String idProject, final boolean useRemoteRepository) throws ToastRuntimeException {
		DAOManager.init(this.mongoDbHost, this.mongoDbPort, this.db);
		final TestPlanImpl lastExecution = DAOManager.getLastTestPlanExecution(name, idProject);
		final TestPlanImpl testPlanTemplate = DAOManager.getTestPlanTemplate(name, idProject);
		if (testPlanTemplate == null) {
			throw new ToastRuntimeException("No reference test plan template found for: " + name);
		}
		updateTestPlanFromPreviousRun((ITestPlan)testPlanTemplate, lastExecution);
		runAndSave(testPlanTemplate, useRemoteRepository);
	}

	private void runAndSave(ITestPlan testPlan, boolean useRemoteRepository) throws ToastRuntimeException {
		try {
			execute(testPlan, useRemoteRepository);
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
		runAndSave(testPlan, useRemoteRepository);
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

	public void execute(final ITestPlan testPlan, final boolean presetRepoFromWebApp) throws IOException {
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
					runner.run(testPage);
					endTest();
				} catch (final Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		if(shouldSendMail()){
			sendEmailReport(testPlan);
		}
		createAndOpenReport(testPlan);
		tearDownEnvironment();
	}


	public boolean shouldSendMail(){
		return false;
	}

	protected void createAndOpenReport(final ITestPlan testPlan) {
		final String path = getReportsFolderPath();
		final String pageName = "testplan_report";

		for (final ICampaign campaign : testPlan.getCampaigns()) {
			for (final ITestPage testPage : campaign.getTestCases()) {
				String testPageHtmlReport = htmlReportGenerator.generatePageHtml(testPage);
				htmlReportGenerator.writeFile(testPageHtmlReport, testPage.getName(), path);
				System.out.println("\nNumber of Success step in test : " + testPage.getTestSuccessNumber() + "\n");
			}
		}

		final String generatePageHtml = projectHtmlReportGenerator.generateProjectReportHtml(testPlan);
		this.projectHtmlReportGenerator.writeFile(generatePageHtml, pageName, path);
		openReport(path, pageName);
	}

	protected void sendLocalEmailReport (final ITestPlan project) {
		for (final ICampaign campaign : project.getCampaigns()) {
			String mailTo = "toto@talanlabs.com";
			String mailFrom = "toast@gmail.com";
			String host = "localhost";
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", host);
			Session session = Session.getDefaultInstance(properties);
			int success = 0;
			int failure = 0;
			int total = 0;
			try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailFrom));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
				message.setSubject("Test report");
				StringBuilder sb = new StringBuilder();

				for (final ITestPage testPage : campaign.getTestCases()) {
					success = testPage.getTestSuccessNumber();
					failure = testPage.getTestFailureNumber();
					total = success + failure;
					String name = testPage.getName();

					sb.append("Test report for "+ name + "\n").append(System.lineSeparator());
					sb.append("Total number of steps : " + total + "\n");
					sb.append("Number of Success steps in test : " + success + "\nNumber of Failure steps in test : " + failure + "\n\n");

				}
				message.setText(sb.toString());
				Transport.send(message);
				System.out.println("Message sent successfully");
			}catch (MessagingException mex) {
				mex.printStackTrace();
			}
		}
	}


	protected void sendEmailReport (final ITestPlan project) {

		final Properties prop = new Properties();

		InputStream resourceAsStream = this.getClass().getResourceAsStream("/toast.properties");

		try (final Reader resourceFileReader = new InputStreamReader(resourceAsStream)) {
			prop.load(resourceFileReader);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}

		for (final ICampaign campaign : project.getCampaigns()) {
			String mailTo = prop.getProperty("mail.user.to");
			String mailFrom = prop.getProperty("mail.user.from");
			String password = prop.getProperty("mail.password");
			String host = prop.getProperty("mail.host");
			String port = prop.getProperty("mail.port");
			String ssl = prop.getProperty("mail.ssl");
			String tls = prop.getProperty("mail.tls");

			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", host);
			prop.put("mail.smtp.socketFactory.port", port);
			prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			prop.put("mail.smtp.socketFactory.fallback", "false");
			prop.setProperty("mail.smtp.starttls.enable", ssl);
	       prop.setProperty("mail.smtp.password", password );
	        prop.setProperty("mail.smtp.auth", "true");
			Session session = Session.getDefaultInstance(properties);
			int success = 0;
			int failure = 0;
			int total = 0;
			try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailFrom));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
				message.setSubject("Test report");
				StringBuilder sb = new StringBuilder();

				for (final ITestPage testPage : campaign.getTestCases()) {
					success = testPage.getTestSuccessNumber();
					failure = testPage.getTestFailureNumber();
					total = success + failure;
					String name = testPage.getName();

					sb.append("Test report for "+ name + "\n").append(System.lineSeparator());
					sb.append("Total number of steps : " + total + "\n");
					sb.append("Number of Success steps in test : " + success + "\nNumber of Failure steps in test : " + failure + "\n\n");

				}
				message.setText(sb.toString());
				Transport.send(message);
				System.out.println("Message sent successfully");
			}catch (MessagingException mex) {
				mex.printStackTrace();
			}
		}
	}

	@Override
	public String getReportsOutputPath(){
		return null;
	}

}
