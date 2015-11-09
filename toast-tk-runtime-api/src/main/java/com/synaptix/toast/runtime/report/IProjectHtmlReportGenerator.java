package com.synaptix.toast.runtime.report;

import com.synaptix.toast.dao.domain.impl.test.block.IProject;


public interface IProjectHtmlReportGenerator {

	String generateProjectReportHtml(String name);

	String generateProjectReportHtml(IProject project);

	String generateProjectReportHtml(IProject iProject, String reportFolderPath);
}
