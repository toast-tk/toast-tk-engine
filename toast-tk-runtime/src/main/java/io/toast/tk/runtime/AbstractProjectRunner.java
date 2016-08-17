package io.toast.tk.runtime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Module;

import io.toast.tk.core.rest.RestUtils;
import io.toast.tk.dao.domain.impl.report.Project;
import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.IProject;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.runtime.dao.DAOManager;
import io.toast.tk.runtime.parse.TestParser;
import io.toast.tk.runtime.report.IHTMLReportGenerator;
import io.toast.tk.runtime.report.IProjectHtmlReportGenerator;

public abstract class AbstractProjectRunner extends AbstractRunner {

    private static final Logger LOG = LogManager.getLogger(AbstractProjectRunner.class);

    private final IHTMLReportGenerator htmlReportGenerator;

    private final IProjectHtmlReportGenerator projectHtmlReportGenerator;

	private String mongoDbHost;

	private int mongoDbPort;

	protected AbstractProjectRunner() {
		super();
		this.projectHtmlReportGenerator = injector.getInstance(IProjectHtmlReportGenerator.class);
		this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
	}

    protected AbstractProjectRunner(
    	final Module extraModule, 
    	final String host,
    	final int port
    ) {
    	this(extraModule);
    	this.mongoDbHost = host;
    	this.mongoDbPort = port;
    }
    
    protected AbstractProjectRunner(
    	final String host,
    	final int port
    ) {
    	this();
    	this.mongoDbHost = host;
    	this.mongoDbPort = port;
    }

    public AbstractProjectRunner(final Module extraModule) {
    	super(extraModule);
    	this.projectHtmlReportGenerator = injector.getInstance(IProjectHtmlReportGenerator.class);
        this.htmlReportGenerator = injector.getInstance(IHTMLReportGenerator.class);
    }
    
	public final void test(IProject project,
            boolean overrideRepoFromWebApp)
            throws Exception {
        execute(project, overrideRepoFromWebApp);
    }
    

	public final void test(
		final String projectName,
		final boolean overrideRepoFromWebApp
	) throws Exception {
		final DAOManager daoManager = DAOManager.getInstance(this.mongoDbHost, this.mongoDbPort);
		final Project lastProject = daoManager.getLastProjectByName(projectName);
		final Project referenceProject = daoManager.getReferenceProjectByName(projectName);
        if (referenceProject == null) {
            throw new IllegalAccessException("No reference project name found for: " + projectName);
        }
        final Project newIterationProject = mergeToNewIteration(lastProject, referenceProject);
        execute(newIterationProject, overrideRepoFromWebApp);
        daoManager.saveProject(newIterationProject);
    }

	public final void testAndStore(final IProject project) throws Exception {
    	testAndStore(project, false);
    }
    
    public final void testAndStore(
    	final IProject project,
    	final boolean overrideRepoFromWebApp
    ) throws Exception {
    	final DAOManager daoManager = DAOManager.getInstance(this.mongoDbHost, this.mongoDbPort);
    	final Project lastProject = daoManager.getLastProjectByName(project.getName());
    	final Project referenceProject = daoManager.getReferenceProjectByName(project.getName());
        if (referenceProject == null) {
        	daoManager.saveProject((Project)project);
        }
        
        final Project newIterationProject;
        if(lastProject != null){
        	newIterationProject = mergeToNewIteration(lastProject, referenceProject);
        }
        else{
        	newIterationProject = daoManager.getReferenceProjectByName(project.getName());
        }
        final Project projectToRun = mergeToRunIteration(newIterationProject,(Project)project);
        execute(projectToRun, overrideRepoFromWebApp); //TODO: merge newIterationProject with
        daoManager.saveProject(projectToRun);
    }
    
    private static Project mergeToRunIteration(
    	final Project lastIterationProject,
    	final Project newIterationProject
    ) {
        if (lastIterationProject.getIteration() == newIterationProject.getIteration()) {
            return newIterationProject;
        }

        //creating a new iteration from history
        newIterationProject.setId(null);
        newIterationProject.setIteration(lastIterationProject.getIteration());
        
        //TO LINEARIZE
        for(final ICampaign newCampaign : newIterationProject.getCampaigns()) {
            for(final ICampaign lastCampaign : lastIterationProject.getCampaigns()) {
                if (newCampaign.getName().equals(lastCampaign.getName())) {
                    for(final ITestPage newPage : newCampaign.getTestCases()) {
                        for(final ITestPage lastPage : lastCampaign.getTestCases()) {
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

    private static Project mergeToNewIteration(
    	final Project lastIterationProject,
    	final Project newIterationProject
    ) {
    	if (lastIterationProject.getIteration() == newIterationProject.getIteration()) {
    		return newIterationProject;
    	}

    	//creating a new iteration from history
    	newIterationProject.setId(null);
    	newIterationProject.setIteration(lastIterationProject.getIteration());
    	//TO LINEARIZE
    	for(final ICampaign newCampaign : newIterationProject.getCampaigns()) {
    		for(final ICampaign lastCampaign : lastIterationProject.getCampaigns()) {
    			if (newCampaign.getName().equals(lastCampaign.getName())) {
    				for(final ITestPage newPage : newCampaign.getTestCases()) {
    					for(ITestPage lastPage : lastCampaign.getTestCases()) {
    						if(newPage.getName().equals(lastPage.getName())) {
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
    	final IProject project,
    	final boolean presetRepoFromWebApp
    )	throws Exception {
    	final TestRunner runner = injector.getInstance(TestRunner.class);
        if(presetRepoFromWebApp) {
        	LOG.debug("Preset repository from webapp rest api...");
        	final String repoWiki = RestUtils.downloadRepositoryAsWiki();
        	final TestParser parser = new TestParser();
        	final ITestPage repoAsTestPageForConvenience = parser.readString(repoWiki, null);
            runner.run(repoAsTestPageForConvenience);
        }
        execute(project, runner);
    }

    private void execute(
    	final IProject project,
    	final TestRunner runner
    ) {
        initEnvironment();
        for(final ICampaign campaign : project.getCampaigns()) {
            for(ITestPage testPage : campaign.getTestCases()) {
                try {
                    beginTest();
                    testPage = runner.run(testPage);
                    endTest();
                } 
                catch(final Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        createAndOpenReport(project);
        tearDownEnvironment();
    }

    private void createAndOpenReport(final IProject project) {
    	final String path = getReportsFolderPath();
        final String pageName = "Project_report";

        for(final ICampaign campaign : project.getCampaigns()) {
            for(final ITestPage testPage : campaign.getTestCases()) {
                String testPageHtmlReport = htmlReportGenerator.generatePageHtml(testPage);
                htmlReportGenerator.writeFile(testPageHtmlReport, testPage.getName(), path);
            }
        }

        final String generatePageHtml = projectHtmlReportGenerator.generateProjectReportHtml(project);
        this.projectHtmlReportGenerator.writeFile(generatePageHtml, pageName, path);
        openReport(path, pageName);
    }
}