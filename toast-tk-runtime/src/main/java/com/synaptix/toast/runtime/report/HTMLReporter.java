package com.synaptix.toast.runtime.report;

import com.synaptix.toast.dao.domain.impl.report.Project;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;

public class HTMLReporter {

	public static final String getProjectHTMLReport(final String name) throws IllegalAccessException {
		final IProjectHtmlReportGenerator projectHtmlReportGenerator = new ThymeLeafProjectHTMLReporter();
		return projectHtmlReportGenerator.generateProjectReportHtml(name);
	}
	
	public static final String getProjectHTMLReport(final Project project) {
		final IProjectHtmlReportGenerator projectHtmlReportGenerator = new ThymeLeafProjectHTMLReporter();
		return projectHtmlReportGenerator.generateProjectReportHtml(project);
	}
	
	public static final String getTestPageHTMLReport(final ITestPage test){
		final ThymeLeafHTMLReporter reporter = new ThymeLeafHTMLReporter();
		return reporter.generatePageHtml(test);
	}
}