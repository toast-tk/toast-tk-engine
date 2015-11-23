package com.synaptix.toast.runtime;

import com.synaptix.toast.dao.domain.impl.test.block.IProject;
import com.synaptix.toast.runtime.report.IHTMLReportGenerator;
import com.synaptix.toast.runtime.report.IProjectHtmlReportGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Injector;
import com.synaptix.toast.core.rest.RestUtils;
import com.synaptix.toast.dao.domain.impl.report.Project;
import com.synaptix.toast.dao.domain.impl.test.block.ICampaign;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.runtime.dao.DAOManager;
import com.synaptix.toast.runtime.parse.TestParser;

public abstract class AbstractProjectRunner extends AbstractRunner {

    private static final Logger LOG = LogManager.getLogger(AbstractProjectRunner.class);
    private final IHTMLReportGenerator htmlReportGenerator;
    private Injector injector;
    private final IProjectHtmlReportGenerator projectHtmlReportGenerator;


    protected AbstractProjectRunner(
            Injector injector)
            throws Exception {
        super();
        this.injector = injector;
        this.projectHtmlReportGenerator = injector.getInstance(IProjectHtmlReportGenerator.class);
        this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
    }

    public final void test(
            String projectName,
            boolean overrideRepoFromWebApp)
            throws Exception {
        Project lastProject = DAOManager.getInstance().getLastProjectByName(projectName);
        Project referenceProject = DAOManager.getInstance().getReferenceProjectByName(projectName);
        if (referenceProject == null) {
            throw new IllegalAccessException("No reference project name found for: " + projectName);
        }
        Project newIterationProject = mergeToNewIteration(lastProject, referenceProject);
        execute(newIterationProject, overrideRepoFromWebApp);
        DAOManager.getInstance().saveProject(newIterationProject);
    }

    private Project mergeToNewIteration(
            Project lastIterationProject,
            Project newIterationProject) {
        if (lastIterationProject.getIteration() == newIterationProject.getIteration()) {
            return newIterationProject;
        }

        //creating a new iteration from history
        newIterationProject.setId(null);
        newIterationProject.setIteration(lastIterationProject.getIteration());
        for (ICampaign newCampaign : newIterationProject.getCampaigns()) {
            for (ICampaign lastCampaign : lastIterationProject.getCampaigns()) {
                if (newCampaign.getIdAsString().equals(lastCampaign.getIdAsString())) {
                    for (ITestPage newPage : newCampaign.getTestCases()) {
                        for (ITestPage lastPage : lastCampaign.getTestCases()) {
                            if (newPage.getIdAsString().equals(lastPage.getIdAsString())) {
                                newPage.setPreviousIsSuccess(lastPage.isPreviousIsSuccess());
                                newPage.setPreviousExecutionTime(lastPage.getPreviousExecutionTime());
                            }
                        }
                    }
                }
            }
        }
        return newIterationProject;
    }

    public void execute(
            IProject project,
            boolean presetRepoFromWebApp)
            throws Exception {
        TestRunner runner = new TestRunner(injector);
        if (presetRepoFromWebApp) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Preset repository from webapp rest api...");
            }
            String repoWiki = RestUtils.downloadRepositoyAsWiki();
            TestParser parser = new TestParser();
            ITestPage repoAsTestPageForConvenience = parser.readString(repoWiki, null);
            runner.run(repoAsTestPageForConvenience, false);
        }
        execute(project, runner);
    }

    private void execute(
            IProject project,
            TestRunner runner)
            throws ClassNotFoundException {
        initEnvironment();
        for (ICampaign campaign : project.getCampaigns()) {
            for (ITestPage testPage : campaign.getTestCases()) {
                try {
                    beginTest();
                    testPage = runner.run(testPage, true);
                    endTest();
                } catch (IllegalAccessException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        createAndOpenReport(project);
        tearDownEnvironment();
    }

    protected void createAndOpenReport(IProject project) {
        String path = getReportsFolderPath();
        final String pageName = "Project_report";

        for (ICampaign campaign : project.getCampaigns()) {
            for (ITestPage testPage : campaign.getTestCases()) {
                String testPageHtmlReport = htmlReportGenerator.generatePageHtml(testPage);
                htmlReportGenerator.writeFile(testPageHtmlReport, testPage.getName(), path);
            }
        }

        String generatePageHtml = projectHtmlReportGenerator.generateProjectReportHtml(project);
        this.projectHtmlReportGenerator.writeFile(generatePageHtml, pageName, path);
        openReport(path, pageName);
    }

}
