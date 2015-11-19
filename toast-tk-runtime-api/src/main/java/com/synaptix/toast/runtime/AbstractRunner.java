package com.synaptix.toast.runtime;

import com.google.inject.Injector;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.runtime.report.IHTMLReportGenerator;
import com.synaptix.toast.runtime.report.IProjectHtmlReportGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractRunner {

    private static final Logger LOG = LogManager.getLogger(AbstractRunner.class);

    public AbstractRunner() {
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
