package com.synaptix.toast.runtime.report;

import java.net.URL;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.synaptix.toast.core.event.TestProgressMessage;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.runtime.IReportUpdateCallBack;

public class DefaultTestProgressReporter {

	private IReportUpdateCallBack reportUpdateCallBack;

	private IHTMLReportGenerator htmlReportGenerator;
	
	public DefaultTestProgressReporter(
		final EventBus bus,
		final IHTMLReportGenerator htmlReportGenerator
	) {
		this.htmlReportGenerator = htmlReportGenerator;
		bus.register(this);
	}
	
	public void setReportCallBack(final IReportUpdateCallBack callback){
		this.reportUpdateCallBack = callback;
	}
	
	@Subscribe
	public void handleTestExecutionProgress(final TestProgressMessage progressMessage){
		handleInlineReport(progressMessage.getPage());
	}
	
	private void handleInlineReport(final ITestPage testPage) {
		final String generatePageHtml = htmlReportGenerator.generatePageHtml(testPage);
		final URL resource = this.getClass().getClassLoader() != null ? this
				.getClass().getClassLoader().getResource("TestResults")
				: null;
		if (resource != null) {
			this.htmlReportGenerator.writeFile(generatePageHtml, testPage.getName(), resource.getPath());
		}
		if (this.reportUpdateCallBack != null) {
			this.reportUpdateCallBack.onUpdate(generatePageHtml);
		}
	}
}