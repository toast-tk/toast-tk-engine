package com.synaptix.toast.runtime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Module;
import com.synaptix.toast.core.rest.RestUtils;
import com.synaptix.toast.dao.domain.impl.report.Project;
import com.synaptix.toast.dao.domain.impl.test.block.ICampaign;
import com.synaptix.toast.dao.domain.impl.test.block.IProject;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.runtime.dao.DAOManager;
import com.synaptix.toast.runtime.parse.TestParser;
import com.synaptix.toast.runtime.report.IHTMLReportGenerator;
import com.synaptix.toast.runtime.report.IProjectHtmlReportGenerator;

public abstract class AbstractProjectRunner extends AbstractRunner {

    private static final Logger LOG = LogManager.getLogger(AbstractProjectRunner.class);
    private final IHTMLReportGenerator htmlReportGenerator;
    private final IProjectHtmlReportGenerator projectHtmlReportGenerator;
	private String mongoDbHost;
	private int mongoDbPort;

	  protected AbstractProjectRunner(
	            )
	           {
	        super();
	        this.projectHtmlReportGenerator = injector.getInstance(IProjectHtmlReportGenerator.class);
	        this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
	    }

    protected AbstractProjectRunner(
            Module extraModule, 
            String host,
            int port
            )
           {
    	this(extraModule);
    	this.mongoDbHost = host;
    	this.mongoDbPort = port;
    }
    
    protected AbstractProjectRunner(
            String host,
            int port
            )
            {
    	this();
    	this.mongoDbHost = host;
    	this.mongoDbPort = port;
    }

    public AbstractProjectRunner(Module extraModule) {
    	super(extraModule);
    	this.projectHtmlReportGenerator = injector.getInstance(IProjectHtmlReportGenerator.class);
        this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
    }

	public final void test(
            String projectName,
            boolean overrideRepoFromWebApp)
            throws Exception {
    	DAOManager daoManager = DAOManager.getInstance(this.mongoDbHost, this.mongoDbPort);
        Project lastProject = daoManager.getLastProjectByName(projectName);
        Project referenceProject = daoManager.getReferenceProjectByName(projectName);
        if (referenceProject == null) {
            throw new IllegalAccessException("No reference project name found for: " + projectName);
        }
        Project newIterationProject = mergeToNewIteration(lastProject, referenceProject);
        execute(newIterationProject, overrideRepoFromWebApp);
        daoManager.saveProject(newIterationProject);
    }
    
    
    
    public final void testAndStore(
            IProject project)
            throws Exception {
    	testAndStore(project, false);
    }
    
    public final void testAndStore(
            IProject project,
            boolean overrideRepoFromWebApp)
            throws Exception {
    	DAOManager daoManager = DAOManager.getInstance(this.mongoDbHost, this.mongoDbPort);
        Project lastProject = daoManager.getLastProjectByName(project.getName());
        Project referenceProject = daoManager.getReferenceProjectByName(project.getName());
        if (referenceProject == null) {
        	daoManager.saveProject((Project)project);
        }
        Project newIterationProject = null;
        if(lastProject != null){
        	newIterationProject = mergeToNewIteration(lastProject, referenceProject);
        }else{
        	newIterationProject = daoManager.getReferenceProjectByName(project.getName());
        }
        Project projectToRun = mergeToRunIteration(newIterationProject,(Project)project);
        execute(projectToRun, overrideRepoFromWebApp); //TODO: merge newIterationProject with
        daoManager.saveProject(projectToRun);
    }
    
    
    private Project mergeToRunIteration(
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
                if (newCampaign.getName().equals(lastCampaign.getName())) {
                    for (ITestPage newPage : newCampaign.getTestCases()) {
                        for (ITestPage lastPage : lastCampaign.getTestCases()) {
                            if (newPage.getName().equals(lastPage.getName())) {
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
                if (newCampaign.getName().equals(lastCampaign.getName())) {
                    for (ITestPage newPage : newCampaign.getTestCases()) {
                        for (ITestPage lastPage : lastCampaign.getTestCases()) {
                            if (newPage.getName().equals(lastPage.getName())) {
                                newPage.setPreviousIsSuccess(lastPage.isSuccess());
                                newPage.setPreviousExecutionTime(lastPage.getExecutionTime());
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
