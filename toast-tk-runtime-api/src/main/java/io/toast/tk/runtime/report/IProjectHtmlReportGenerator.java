package io.toast.tk.runtime.report;

import io.toast.tk.dao.domain.impl.test.block.IProject;

public interface IProjectHtmlReportGenerator {

    void writeFile(final String report, final String pageName, final String reportFolderPath);

    String generateProjectReportHtml(final String name) throws IllegalAccessException;

    String generateProjectReportHtml(final IProject project);

    String generateProjectReportHtml(final IProject iProject, final String reportFolderPath) throws IllegalAccessException;
}