package com.synaptix.toast.runtime.report;

import com.synaptix.toast.dao.domain.impl.report.Project;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.runtime.dao.DAOManager;

public class HTMLReporter {

	public static final String getProjectHTMLReport(
		String name) throws IllegalAccessException {
		IProjectHtmlReportGenerator projectHtmlReportGenerator = new ThymeLeafProjectHTMLReporter();
		String projectReportHtml = projectHtmlReportGenerator.generateProjectReportHtml(name);
		return projectReportHtml;
	}
	
	public static final String getProjectHTMLReport(
		Project project) {
		IProjectHtmlReportGenerator projectHtmlReportGenerator = new ThymeLeafProjectHTMLReporter();
		String projectReportHtml = projectHtmlReportGenerator.generateProjectReportHtml(project);
		return projectReportHtml;
	}
	
	public static final String getTestPageHTMLReport(ITestPage test){
		ThymeLeafHTMLReporter reporter = new ThymeLeafHTMLReporter();
		String outputHtml = reporter.generatePageHtml(test);
		return outputHtml;
	}
	
	public static void main(String[] args) {
		try {
			DAOManager.getInstance("10.23.252.131", 27017);
			String projectHTMLReport = HTMLReporter.getProjectHTMLReport("rus.3.7.campaign.script");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
