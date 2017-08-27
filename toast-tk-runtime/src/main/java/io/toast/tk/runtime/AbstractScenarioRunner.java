package io.toast.tk.runtime;

import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import io.toast.tk.core.annotation.EngineEventBus;
import io.toast.tk.core.rest.RestUtils;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.runtime.parse.FileHelper;
import io.toast.tk.runtime.parse.TestParser;
import io.toast.tk.runtime.report.DefaultTestProgressReporter;
import io.toast.tk.runtime.report.IHTMLReportGenerator;
import io.toast.tk.runtime.utils.ResultObject;
import io.toast.tk.runtime.utils.RunUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScenarioRunner extends AbstractRunner {

	private static final Logger LOG = LogManager.getLogger(AbstractScenarioRunner.class);

	private boolean presetRepoFromWebApp = false;

	private ITestPage localRepositoryTestPage;
	private TestRunner runner;

	private IHTMLReportGenerator htmlReportGenerator;

	private DefaultTestProgressReporter progressReporter;

	private String apiKey;
	
	public TestRunner getTestRunner() {
		return runner;
	}

	protected AbstractScenarioRunner(final Injector injector) {
		super(injector);
		this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
		final EventBus eventBus = injector.getInstance(Key.get(EventBus.class, EngineEventBus.class));
		this.progressReporter = new DefaultTestProgressReporter(eventBus);
	}

	protected AbstractScenarioRunner(final Module... m) {
		super(m);
		this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
		final EventBus eventBus = injector.getInstance(Key.get(EventBus.class, EngineEventBus.class));
		this.progressReporter = new DefaultTestProgressReporter(eventBus);
	}

	protected AbstractScenarioRunner() {
		super();
		this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
		final EventBus eventBus = injector.getInstance(Key.get(EventBus.class, EngineEventBus.class));
		this.progressReporter = new DefaultTestProgressReporter(eventBus);
	}

	public final void run(
			final String... scenarios
	) throws Exception {
		runScenario(scenarios);
	}

	public void runScenario(
			final String... scenarios
	) throws Exception {
		final List<ITestPage> testPages = new ArrayList<>();
		initEnvironment();
		for (final String fullFileName : scenarios) {
			LOG.info("Start main test parser: {}", fullFileName);

			List<String> lines = FileHelper.getScript(fullFileName);
			String fileName = fullFileName.startsWith("C:") ? fullFileName.split("\\\\")[fullFileName.split("\\\\").length - 1] : fullFileName;
			final ITestPage result = runTestPage(new TestParser().parse(lines, fileName));
			testPages.add(result);
		}

		tearDownEnvironment();

		LOG.info("{}file(s) processed", scenarios.length);
		ResultObject res = RunUtils.getResult(testPages);
		RunUtils.print(res.getTotalErrors(), res.getTotalSuccess(), res.getTotalTechnical(), res.getFilesWithErrorsList());
		if (shouldSendMail()) {
			mailReportSender.sendMailReport(testPages, res);
		}
	}

	public final void runRemote(String apiKey,
			final String... scenarios
	) throws Exception {
		this.presetRepoFromWebApp = true;
		run(scenarios);
	}

	public final void runRemoteScript(String apiKey,
			final String script
	) throws Exception {
		this.presetRepoFromWebApp = true;
		runScript(script);
	}

	public void runLocalScript(
			final String wikiScenario,
			final String repoWiki,
			final IReportUpdateCallBack callback
	) throws IOException {
		this.progressReporter.setReportCallBack(callback);
		final TestParser parser = new TestParser();
		this.localRepositoryTestPage = parser.readString(repoWiki, null);
		runScript(wikiScenario);
	}

	private ITestPage runScript(final String script) throws IOException {
		final TestParser testParser = new TestParser();
		ITestPage testPage = testParser.readString(script, null);
		return runTestPage(testPage);
	}

	public ITestPage runTestPage(final ITestPage testPage) throws IOException {
		runner = injector.getInstance(TestRunner.class);
		if (this.presetRepoFromWebApp) {
			final String repoWiki = RestUtils.downloadRepositoryAsWiki(this.apiKey);
			final TestParser parser = new TestParser();
			final ITestPage repoAsTestPageForConvenience = parser.readString(repoWiki, null);
			runner.run(repoAsTestPageForConvenience);
		} else if (this.localRepositoryTestPage != null) {
			runner.run(this.localRepositoryTestPage);
		}
		beginTest();
		ITestPage result = runner.run(testPage);
		
		List<ITestPage> testPages = new ArrayList<ITestPage>();
		testPages.add(testPage);
		ResultObject res = RunUtils.getResult(testPages);
		RunUtils.print(res.getTotalErrors(), res.getTotalSuccess(), res.getTotalTechnical(), res.getFilesWithErrorsList());
		if (shouldSendMail()) {
			mailReportSender.sendMailReport(testPages, res);
		}
		
		createAndOpenReport(result);
		endTest();
		return result;
	}

	private void createAndOpenReport(
			final ITestPage testPage
	) {
		final String generatePageHtml = htmlReportGenerator.generatePageHtml(testPage);
		final String path = getReportsOutputPath() == null ? getReportsFolderPath(): getReportsOutputPath();
		final String pageName = testPage.getName();
		htmlReportGenerator.writeFile(generatePageHtml, pageName, path);
		openReport(path, pageName);
	}

	public void kill() {
		if(this.runner != null){
			this.runner.kill();
		}
	}

	@Override
	public String getReportsOutputPath(){
		return null;
	}

}
