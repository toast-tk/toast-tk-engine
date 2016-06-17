package com.synaptix.toast.runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.synaptix.toast.core.annotation.EngineEventBus;
import com.synaptix.toast.core.rest.RestUtils;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.runtime.parse.TestParser;
import com.synaptix.toast.runtime.report.DefaultTestProgressReporter;
import com.synaptix.toast.runtime.report.IHTMLReportGenerator;
import com.synaptix.toast.runtime.utils.RunUtils;

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
			final File file = readTestFile(fileName);
			final ITestPage result = runScript(file, fileName);
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
		runScript(null, script);
	}

	public void runLocalScript(
			final String wikiScenario,
			final String repoWiki,
			final IReportUpdateCallBack callback
	) throws IOException {
		this.progressReporter.setReportCallBack(callback);
		final TestParser parser = new TestParser();
		this.localRepositoryTestPage = parser.readString(repoWiki, null);
		runScript(null, wikiScenario);
	}

	private File readTestFile(
			final String fileName
	) throws IOException, URISyntaxException {
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName);

		final File tempFile = File.createTempFile("test", "tmp");
		tempFile.deleteOnExit();
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(resourceAsStream, out);
		}
		return tempFile;

//		final URI uri = resource.toURI();
//		final Map<String, String> env = new HashMap<>();
//		final String[] array = uri.toString().split("!");
//		final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env);
//		final Path path = fs.getPath(array[1]);
	}

    private ITestPage runScript(
    	final File file,
    	final String script
    ) throws IOException {
    	final TestParser testParser = new TestParser();
        ITestPage result = file == null ? testParser.readString(script, null) : testParser.parse(file.getPath());
        final TestRunner runner = injector.getInstance(TestRunner.class);
        if(this.presetRepoFromWebApp) {
        	final String repoWiki = RestUtils.downloadRepositoryAsWiki();
        	final TestParser parser = new TestParser();
        	final ITestPage repoAsTestPageForConvenience = parser.readString(repoWiki, null);
            runner.run(repoAsTestPageForConvenience);
        } 
        else if (this.localRepositoryTestPage != null) {
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