package io.toast.tk.runtime;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.toast.tk.adapter.ActionAdapterCollector;
import io.toast.tk.adapter.FixtureService;
import io.toast.tk.adapter.constant.AdaptersConfig;
import io.toast.tk.adapter.constant.AdaptersConfigProvider;
import io.toast.tk.core.guice.AbstractActionAdapterModule;
import io.toast.tk.runtime.module.EngineModule;
import io.toast.tk.runtime.report.IMailReportSender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class AbstractRunner {

	private static final Logger LOG = LogManager.getLogger(AbstractRunner.class);

	private List<FixtureService> listAvailableServicesByReflection;

	protected final IMailReportSender mailReportSender;
	
	protected final Injector injector;

	public AbstractRunner() {
		listAvailableServicesByReflection = ActionAdapterCollector.listAvailableServicesByReflection();
		LOG.info("Found adapters: {}", listAvailableServicesByReflection.size());
		this.injector = Guice.createInjector(new AbstractActionAdapterModule() {
			@Override
			protected void configure() {
				install(new EngineModule());
				listAvailableServicesByReflection.forEach(f -> bindActionAdapter(f.clazz));
			}
		});
		this.mailReportSender = injector.getInstance(IMailReportSender.class);
	}

	public AbstractRunner(final Injector injector) {
		this.injector = injector;
		this.mailReportSender = injector.getInstance(IMailReportSender.class);
	}

	public AbstractRunner(final Module... extraModules) {
		listAvailableServicesByReflection = ActionAdapterCollector.listAvailableServicesByReflection();
		LOG.info("Found adapters: {}", listAvailableServicesByReflection.size());
		this.injector = Guice.createInjector(new AbstractActionAdapterModule() {
			@Override
			protected void configure() {
				install(new EngineModule());
				for(Module module: extraModules){
					install(module);
				}
				listAvailableServicesByReflection.forEach(f -> bindActionAdapter(f.clazz));
			}
		});
		this.mailReportSender = injector.getInstance(IMailReportSender.class);
	}

	public abstract void tearDownEnvironment();

	public abstract void beginTest();

	public abstract void endTest();

	public abstract void initEnvironment();

	public abstract String getReportsOutputPath();

	/**
	 * Creates the test reports folder, if it does not exists, and returns its path.
	 * The folder is created in the current execution location, under /target/toast-test-results
	 *
	 * @return Path as a string
	 */
	protected static String getReportsFolderPath() {
		AdaptersConfigProvider configProvider = new AdaptersConfigProvider();
		AdaptersConfig config = configProvider.get();
		String reportsFolderPath = config.getReportsFolderPath();
		if (reportsFolderPath != null) {
			return reportsFolderPath;
		}

		final StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append(System.getProperty("user.home"));
		pathBuilder.append(File.separator);
		pathBuilder.append(".toast");
		pathBuilder.append(File.separator);
		pathBuilder.append("target");
		pathBuilder.append(File.separator);
		pathBuilder.append("toast-test-results");
		final Path currentRelativePath = Paths.get(pathBuilder.toString());

		final File file = new File(currentRelativePath.toUri());
		if (!file.exists()) {
			final boolean mkdirsResult = file.mkdirs();
			if (!mkdirsResult) {
				LOG.info("Report folder creation failed");
				return null;
			}
		}
		final String path = currentRelativePath.toAbsolutePath().toString();
		LOG.info("Report generated in folder: {}", path);
		return path;
	}

	/**
	 * Checks if report must be sent by mail. See "mail.send" in toast.properties.
	 *
	 * @return True if the report must be sent by mail.
	 */
	protected boolean shouldSendMail() {
		return getConfig().isMailSendReport();
	}

	private AdaptersConfig getConfig() {
		return new AdaptersConfigProvider().get();
	}
	
	protected static void openReport(
			final String path,
			final String pageName
	) {
		try {
			if (!Boolean.getBoolean("java.awt.headless")) {
				final File htmlFile = new File(path + File.separatorChar + pageName + ".html");
				Desktop.getDesktop().browse(htmlFile.toURI());
			}
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
