package com.synaptix.toast.runtime;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.synaptix.toast.adapter.ActionAdapterCollector;
import com.synaptix.toast.adapter.FixtureService;
import com.synaptix.toast.core.guice.AbstractActionAdapterModule;
import com.synaptix.toast.runtime.module.EngineModule;

public abstract class AbstractRunner {

    private static final Logger LOG = LogManager.getLogger(AbstractRunner.class);

    protected List<FixtureService> listAvailableServicesByReflection;

    protected final Injector injector;

    public AbstractRunner() {
    	listAvailableServicesByReflection = ActionAdapterCollector.listAvailableServicesByReflection();
		System.out.println("Found adapters: " + listAvailableServicesByReflection.size());
    	injector = Guice.createInjector(new AbstractActionAdapterModule(){
			@Override
			protected void configure() {
				install(new EngineModule());
				listAvailableServicesByReflection.forEach(f -> bindActionAdapter(f.clazz));
			}
    	});
    }

    public AbstractRunner(Injector i) {
    	injector = i;
    }

	public AbstractRunner(Module extraModule) {
		listAvailableServicesByReflection = ActionAdapterCollector.listAvailableServicesByReflection();
		System.out.println("Found adapters: " + listAvailableServicesByReflection.size());
		injector = Guice.createInjector(new AbstractActionAdapterModule(){
			@Override
			protected void configure() {
				install(new EngineModule());
				install(extraModule);
				listAvailableServicesByReflection.forEach(f -> bindActionAdapter(f.clazz));
			}
    	});
	}

	public abstract void tearDownEnvironment();

    public abstract void beginTest();

    public abstract void endTest();

    public abstract void initEnvironment();

    /**
     * Creates the test reports folder, if it does not exists, and returns its path.
     * The folder is created in the current execution location, under /target/toast-test-results
     *
     * @return Path as a string
     */
    protected String getReportsFolderPath() {
        Path currentRelativePath = Paths.get("target/toast-test-results");

        File file = new File(currentRelativePath.toUri());
        if (!file.exists()) {
            boolean mkdirsResult = file.mkdirs();
            if (!mkdirsResult) {
                System.out.println("Report folder creation failed");
                return null;
            }
        }
        String path = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Report generated in folder: " + path);
        return path;
    }

    protected void openReport(String path, String pageName) {
        try {
            if (!Boolean.getBoolean("java.awt.headless")) {
                File htmlFile = new File(path + File.separatorChar + pageName + ".html");
                Desktop.getDesktop().browse(htmlFile.toURI());
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
