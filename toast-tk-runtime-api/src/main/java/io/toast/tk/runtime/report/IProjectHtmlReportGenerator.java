package io.toast.tk.runtime.report;

import io.toast.tk.dao.domain.impl.test.block.ITestPlan;

public interface IProjectHtmlReportGenerator {

    void writeFile(final String report, final String pageName, final String reportFolderPath);

    String generateProjectReportHtml(final String name, String idProject) throws IllegalAccessException;

    String generateProjectReportHtml(final ITestPlan project);

    String generateProjectReportHtml(final ITestPlan iProject, final String reportFolderPath) throws IllegalAccessException;
}