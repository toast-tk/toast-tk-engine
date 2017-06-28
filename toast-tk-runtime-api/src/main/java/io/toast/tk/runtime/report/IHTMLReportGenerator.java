package io.toast.tk.runtime.report;

import io.toast.tk.dao.domain.impl.test.block.ITestPage;

public interface IHTMLReportGenerator {

	String generatePageHtml(final ITestPage testPage);

	void writeFile(
		final String generatePageHtml,
		final String pageName,
		final String path
	);
}