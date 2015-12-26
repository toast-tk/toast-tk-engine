package com.synaptix.toast.runtime.report;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.synaptix.toast.core.event.TestProgressMessage;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.runtime.IReportUpdateCallBack;

public class DefaultTestProgressReporter {

	private static final Logger LOG = LogManager.getLogger(DefaultTestProgressReporter.class);
	private IReportUpdateCallBack reportUpdateCallBack;
	private IHTMLReportGenerator htmlReportGenerator;
	
	public DefaultTestProgressReporter(EventBus bus,
			IHTMLReportGenerator htmlReportGenerator){
		this.htmlReportGenerator = htmlReportGenerator;
		bus.register(this);
	}
	
	public void setReportCallBack(IReportUpdateCallBack callback){
		this.reportUpdateCallBack = callback;
	}
	
	@Subscribe
	public void handleTestExecutionProgress(TestProgressMessage progressMessage){
		try {
			handleInlineReport(progressMessage.getPage());
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	private void handleInlineReport(ITestPage testPage)
			throws IllegalAccessException {
		final String generatePageHtml = htmlReportGenerator.generatePageHtml(testPage);
		final URL resource = this.getClass().getClassLoader() != null ? this
				.getClass().getClassLoader().getResource("TestResults")
				: null;
		if (resource != null) {
			this.htmlReportGenerator.writeFile(generatePageHtml,
					testPage.getName(), resource.getPath());
		}
		if (this.reportUpdateCallBack != null) {
			this.reportUpdateCallBack.onUpdate(generatePageHtml);
		}
	}

}