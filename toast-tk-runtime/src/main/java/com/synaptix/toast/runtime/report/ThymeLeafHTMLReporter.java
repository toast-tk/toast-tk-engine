package com.synaptix.toast.runtime.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

import org.apache.commons.lang.LocaleUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.dao.domain.impl.test.block.TestPage;

/**
 * http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html
 * 
 */
public class ThymeLeafHTMLReporter implements IHTMLReportGenerator {

	@Override
	public void writeFile(
		String report,
		String pageName,
		String reportFolderPath) {
		try {
			FileWriter fstream = new FileWriter(reportFolderPath + File.separatorChar + pageName + ".html");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(report);
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String generatePageHtml(
		ITestPage test) {
		TemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCharacterEncoding("UTF-8");
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		Locale locale = LocaleUtils.toLocale("fr");
		final Context ctx = new Context(locale);
		ctx.setVariable("test", test);
		String htmlOutput = templateEngine.process("new_test_report_template.html", ctx);
		return htmlOutput;
	}
}
