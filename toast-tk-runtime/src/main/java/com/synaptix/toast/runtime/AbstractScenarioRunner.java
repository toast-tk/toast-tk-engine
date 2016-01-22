package com.synaptix.toast.runtime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    protected AbstractScenarioRunner(Injector i) {
    	super(i);
        this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
        EventBus eventBus = injector.getInstance(Key.get(EventBus.class, EngineEventBus.class));
        this.progressReporter = new DefaultTestProgressReporter(eventBus, htmlReportGenerator);
    }
    
    
    protected AbstractScenarioRunner(Module m) {
    	super(m);
        this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
        EventBus eventBus = injector.getInstance(Key.get(EventBus.class, EngineEventBus.class));
        this.progressReporter = new DefaultTestProgressReporter(eventBus, htmlReportGenerator);
    }

    
    protected AbstractScenarioRunner() {
    	super();
        this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
        EventBus eventBus = injector.getInstance(Key.get(EventBus.class, EngineEventBus.class));
        this.progressReporter = new DefaultTestProgressReporter(eventBus, htmlReportGenerator);
    }

    public final void run(
            String... scenarios)
            throws Exception {
        runScenario(scenarios);
    }

    public final void runScenario(
            String... scenarios)
            throws Exception {
        List<ITestPage> testPages = new ArrayList<>();
        initEnvironment();
        for (String fileName : scenarios) {
            LOG.info("Start main test parser: " + fileName);
            File file = readTestFile(fileName);
            ITestPage result = runScript(file, fileName);
            testPages.add(result);
        }
        tearDownEnvironment();
        LOG.info(scenarios.length + "file(s) processed");
        RunUtils.printResult(testPages);
    }

    public final void runRemote(
            String... scenarios)
            throws Exception {
        this.presetRepoFromWebApp = true;
        run(scenarios);
    }

    public final void runRemoteScript(
            String script)
            throws Exception {
        this.presetRepoFromWebApp = true;
        runScript(null, script);
    }

    public void runLocalScript(
            String wikiScenario,
            String repoWiki,
            IReportUpdateCallBack callback)
            throws IllegalAccessException, ClassNotFoundException, IOException {
        this.progressReporter.setReportCallBack(callback);
        TestParser parser = new TestParser();
        this.localRepositoryTestPage = parser.readString(repoWiki, null);
        runScript(null, wikiScenario);
    }

    private File readTestFile(
            String fileName) throws IOException {
        URL resource = this.getClass().getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new FileNotFoundException(fileName);
        }
        return new File(resource.getFile());
    }

    private ITestPage runScript(
            File file,
            String script)
            throws IllegalAccessException, ClassNotFoundException, IOException {
        TestParser testParser = new TestParser();
        ITestPage result = file == null ? testParser.readString(script, null) : testParser.parse(file.getPath());
        TestRunner runner = new TestRunner(injector);
        if (this.presetRepoFromWebApp) {
            String repoWiki = RestUtils.downloadRepositoyAsWiki();
            TestParser parser = new TestParser();
            ITestPage repoAsTestPageForConvenience = parser.readString(repoWiki, null);
            runner.run(repoAsTestPageForConvenience, false);
        } else if (this.localRepositoryTestPage != null) {
            runner.run(this.localRepositoryTestPage, false);
        }
        beginTest();
        result = runner.run(result, true);
        createAndOpenReport(result);
        endTest();
        return result;
    }

    protected void createAndOpenReport(
            ITestPage testPage) {
        String generatePageHtml = htmlReportGenerator.generatePageHtml(testPage);
        String path = getReportsFolderPath();
        final String pageName = testPage.getName();
        this.htmlReportGenerator.writeFile(generatePageHtml, pageName, path);
        openReport(path, pageName);
    }

}
