package io.toast.tk.runtime.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.LocaleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.runtime.report.IHTMLReportGenerator;

/**
 * http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html
 */
public class ThymeLeafHTMLReporter implements IHTMLReportGenerator {

	private static final Logger LOG = LogManager.getLogger(ThymeLeafHTMLReporter.class);

	@Override
	public void writeFile(
			final String report,
			final String pageName,
			final String reportFolderPath
	){
		final String fileFullPath = reportFolderPath + File.separatorChar + pageName + ".html";
		final File file = new File(fileFullPath);
		final File parent_directory = file.getParentFile();
		if (null != parent_directory){
		    parent_directory.mkdirs();
		}
		try (final BufferedWriter out = new BufferedWriter(new FileWriterWithEncoding(fileFullPath, StandardCharsets.UTF_8))) {
			out.write(report);
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public String generatePageHtml(final ITestPage test) {
		final TemplateResolver templateResolver = buildTemplateResolver();
		final TemplateEngine templateEngine = buildTemplateResolver(templateResolver);
		final Locale locale = LocaleUtils.toLocale("fr");
		final Context ctx = buildContext(test, locale);
		return templateEngine.process("new_test_report_template.html", ctx);
	}

	private static Context buildContext(final ITestPage test, final Locale locale) {
		final Context ctx = new Context(locale);
		ctx.setVariable("test", test);
		return ctx;
	}

	private static TemplateEngine buildTemplateResolver(final TemplateResolver templateResolver) {
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
}