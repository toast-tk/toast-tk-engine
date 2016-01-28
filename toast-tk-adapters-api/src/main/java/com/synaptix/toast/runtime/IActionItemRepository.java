package com.synaptix.toast.runtime;

import java.util.Collection;
import java.util.Map;

import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.dao.domain.api.test.ITestResult;

public interface IActionItemRepository {

	public IFeedableSwingPage getSwingPage(
		String entityName);

	public Collection<IFeedableSwingPage> getSwingPages();

	public Collection<IFeedableWebPage> getWebPages();

	public void addSwingPage(
		String fixtureName);

	public void addPage(
		String fixtureName);

	public ITestResult addClass(
		String className,
		String testName,
		String searchBy);


	public Class<?> getService(
		String fixtureName);

	public IFeedableWebPage getPage(
		String fixtureName);

	public void setUserVariables(
		Map<String, Object> userVariables);

	public Map<String, Object> getUserVariables();

	public void addPage(String fixtureName, IFeedableWebPage webPage);
}
