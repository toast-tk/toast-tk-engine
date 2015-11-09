package com.synaptix.toast.runtime;

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
	private Injector injector;

	protected AbstractProjectRunner(
		Injector injector)
		throws Exception {
		this.injector = injector;
	}

	public final void test(
		String projectName,
		boolean overrideRepoFromWebApp)
		throws Exception {
		Project lastProject = DAOManager.getInstance().getLastProjectByName(projectName);
		Project referenceProject = DAOManager.getInstance().getReferenceProjectByName(projectName);
		if(referenceProject == null){
			throw new IllegalAccessException("No reference project name found for: " + projectName);
		}
		Project newIterationProject = mergeToNewIteration(lastProject, referenceProject);
		execute(newIterationProject, overrideRepoFromWebApp);
		DAOManager.getInstance().saveProject(newIterationProject);
	}

	private Project mergeToNewIteration(
		Project lastIterationProject,
		Project newIterationProject) {
		if(lastIterationProject.getIteration() == newIterationProject.getIteration()){
			return newIterationProject;
		}
		
		//creating a new iteration from history
		newIterationProject.setId(null);
		newIterationProject.setIteration(lastIterationProject.getIteration());
		for(ICampaign newCampaign: newIterationProject.getCampaigns()){
			for(ICampaign lastCampaign: lastIterationProject.getCampaigns()){
				if(newCampaign.getIdAsString().equals(lastCampaign.getIdAsString())){
					for(ITestPage newPage: newCampaign.getTestCases()){
						for(ITestPage lastPage: lastCampaign.getTestCases()){
							if(newPage.getIdAsString().equals(lastPage.getIdAsString())){
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

	private void execute(
		Project project,
		boolean presetRepoFromWebApp)
		throws Exception {
		TestRunner runner = new TestRunner(injector);
		if(presetRepoFromWebApp) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("Preset repository from webapp rest api...");
			}
			String repoWiki = RestUtils.downloadRepositoyAsWiki();
			TestParser parser = new TestParser();
			ITestPage repoAsTestPageForConveniency = parser.readString(repoWiki, null);
			runner.run(repoAsTestPageForConveniency, false);
		}
		execute(project, runner);
	}

	private void execute(
		Project project,
		TestRunner runner)
		throws ClassNotFoundException {
		initEnvironment();
		for(ICampaign campaign : project.getCampaigns()) {
			for(ITestPage testPage : campaign.getTestCases()) {
				try {
					beginTest();
					testPage = runner.run(testPage, true);
					endTest();
				}
				catch(IllegalAccessException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		tearDownEnvironment();
	}
}
