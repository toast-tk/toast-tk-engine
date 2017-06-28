package io.toast.tk.runtime.report;

import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.runtime.report.IProjectHtmlReportGenerator;

public class HTMLReporter {

	public static final String getProjectHTMLReport(final String name, final String idProject) throws IllegalAccessException {
		final IProjectHtmlReportGenerator projectHtmlReportGenerator = new ThymeLeafProjectHTMLReporter();
		return projectHtmlReportGenerator.generateProjectReportHtml(name, idProject);
	}
	
	public static final String getProjectHTMLReport(final TestPlanImpl project) {
		final IProjectHtmlReportGenerator projectHtmlReportGenerator = new ThymeLeafProjectHTMLReporter();
		return projectHtmlReportGenerator.generateProjectReportHtml(project);
	}
	
	public static final String getTestPageHTMLReport(final ITestPage test){
		final ThymeLeafHTMLReporter reporter = new ThymeLeafHTMLReporter();
		return reporter.generatePageHtml(test);
	}

}
