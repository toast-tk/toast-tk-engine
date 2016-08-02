package com.synaptix.toast.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.synaptix.toast.core.annotation.EngineEventBus;
import com.synaptix.toast.core.rest.RestUtils;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.dao.domain.impl.test.block.TestPage;
import com.synaptix.toast.runtime.parse.ScriptHelper;
import com.synaptix.toast.runtime.parse.TestParser;
import com.synaptix.toast.runtime.report.DefaultTestProgressReporter;
import com.synaptix.toast.runtime.report.IHTMLReportGenerator;
import com.synaptix.toast.runtime.utils.RunUtils;

import sun.font.Script;

public abstract class AbstractScenarioRunner extends AbstractRunner {

	private static final Logger LOG = LogManager.getLogger(AbstractScenarioRunner.class);

	private boolean presetRepoFromWebApp = false;

	private ITestPage localRepositoryTestPage;

	private IHTMLReportGenerator htmlReportGenerator;

	private DefaultTestProgressReporter progressReporter;

	protected AbstractScenarioRunner(final Injector injector) {
		super(injector);
		this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
		final EventBus eventBus = injector.getInstance(Key.get(EventBus.class, EngineEventBus.class));
		this.progressReporter = new DefaultTestProgressReporter(eventBus);
	}

	protected AbstractScenarioRunner(final Module m) {
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

	private void runScenario(
			final String... scenarios
	) throws Exception {
		final List<ITestPage> testPages = new ArrayList<>();
		initEnvironment();
		for (final String fileName : scenarios) {
			LOG.info("Start main test parser: {}", fileName);

			List<String> lines = ScriptHelper.getScript(fileName);
			final ITestPage result = runTestPage(new TestParser().parse(lines, fileName));
			testPages.add(result);
		}

		tearDownEnvironment();

		LOG.info("{}file(s) processed", scenarios.length);
		RunUtils.printResult(testPages);
	}

	public final void runRemote(
			final String... scenarios
	) throws Exception {
		this.presetRepoFromWebApp = true;
		run(scenarios);
	}

	public final void runRemoteScript(
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

	private InputStream readTestFile(final String fileName) throws IOException, URISyntaxException {
		return this.getClass().getClassLoader().getResourceAsStream(fileName);
	}

	private ITestPage runScript(final String script) throws IOException {
		final TestParser testParser = new TestParser();
		ITestPage result = testParser.readString(script, null);
		return runTestPage(result);
	}

	private ITestPage runTestPage(ITestPage result) throws IOException {
		final TestRunner runner = injector.getInstance(TestRunner.class);
		if (this.presetRepoFromWebApp) {
			final String repoWiki = RestUtils.downloadRepositoryAsWiki();
			final TestParser parser = new TestParser();
			final ITestPage repoAsTestPageForConvenience = parser.readString(repoWiki, null);
			runner.run(repoAsTestPageForConvenience);
		} else if (this.localRepositoryTestPage != null) {
			runner.run(this.localRepositoryTestPage);
		}
		beginTest();
		result = runner.run(result);
		createAndOpenReport(result);
		endTest();
		return result;
	}

	private void createAndOpenReport(
			final ITestPage testPage
	) {
		final String generatePageHtml = htmlReportGenerator.generatePageHtml(testPage);
		final String path = getReportsFolderPath();
		final String pageName = testPage.getName();
		htmlReportGenerator.writeFile(generatePageHtml, pageName, path);
		openReport(path, pageName);
	}
}