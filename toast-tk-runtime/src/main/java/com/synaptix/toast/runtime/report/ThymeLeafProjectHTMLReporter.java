package com.synaptix.toast.runtime.report;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.synaptix.toast.dao.domain.impl.report.Project;
import com.synaptix.toast.dao.domain.impl.test.block.IProject;
import com.synaptix.toast.runtime.dao.DAOManager;

/**
 * http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html
 */
public class ThymeLeafProjectHTMLReporter implements IProjectHtmlReportGenerator {

	private static final Logger LOG = LogManager.getLogger(ThymeLeafProjectHTMLReporter.class);
	
    @Override
    public void writeFile(
    	final String report, 
    	final String pageName,
    	final String reportFolderPath
    ) {
        try(final BufferedWriter out = new BufferedWriter(new FileWriter(reportFolderPath + SystemUtils.FILE_SEPARATOR + pageName + ".html"));) {
            out.write(report);
        } 
        catch(final Exception e) {
           LOG.error(e.getMessage(), e);
        }
    }

    public static String generateHtmlReport(
    	final Project project,
    	final List<Project> projectHistory
    ) {
        final TemplateResolver templateResolver = buildTemplateResolver();
        final TemplateEngine templateEngine = buildTemplateEngine(templateResolver);
        final Locale locale = LocaleUtils.toLocale("fr");
        final Context ctx = buildContext(project, projectHistory, locale);
        return templateEngine.process("new_project_report_template.html", ctx);
    }

	private static Context buildContext(
		final Project project,
		final List<Project> projectHistory, 
		final Locale locale
	) {
		final Context ctx = new Context(locale);
        ctx.setVariable("project", project);
        ctx.setVariable("projectsHistory", projectHistory);
		return ctx;
	}

	private static TemplateEngine buildTemplateEngine(
			final TemplateResolver templateResolver
	) {
		final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
		return templateEngine;
	}

	private static TemplateResolver buildTemplateResolver() {
		final TemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}

    @Override
    public String generateProjectReportHtml(final String name) throws IllegalAccessException {
    	final Project project = DAOManager.getLastProjectByName(name);
        List<Project> projectHistory = DAOManager.getProjectHistory(project);
        return generateHtmlReport(project, projectHistory);
    }

    @Override
    public String generateProjectReportHtml(final IProject iProject) {
    	final Project project = (Project) iProject;
    	List<Project> projectHistory = null;
        try{
        	projectHistory = DAOManager.getProjectHistory(project);
        }
        catch(final Exception e){
        	LOG.error(e.getMessage(), e);
        }
        return generateHtmlReport(project, projectHistory == null ? Collections.emptyList() : projectHistory);
    }

    @Override
    public String generateProjectReportHtml(
    	final IProject iProject, 
    	final String reportFolderPath
    ) throws IllegalAccessException {
        final ThymeLeafProjectHTMLReporter reporter = new ThymeLeafProjectHTMLReporter();
        final Project project = (Project) iProject;
        final List<Project> projectHistory = DAOManager.getProjectHistory(project);
        final String generateHtmlReport = ThymeLeafProjectHTMLReporter.generateHtmlReport(project, projectHistory);
        reporter.writeFile(generateHtmlReport, project.getName(), reportFolderPath);
        return generateHtmlReport;
    }
}