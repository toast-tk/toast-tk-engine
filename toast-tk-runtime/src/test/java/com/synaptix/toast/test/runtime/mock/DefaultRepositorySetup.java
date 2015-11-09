package com.synaptix.toast.test.runtime.mock;

import java.util.Collection;
import java.util.Map;

import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.runtime.IActionItemRepository;

public class DefaultRepositorySetup implements IActionItemRepository {

	private Map<String, Object> userVariables;

	@Override
	public IFeedableSwingPage getSwingPage(
		String entityName) {
		return null;
	}

	@Override
	public Collection<IFeedableSwingPage> getSwingPages() {
		return null;
	}

	@Override
	public void addSwingPage(
		String fixtureName) {
	}

	@Override
	public void addPage(
		String fixtureName) {
	}

	@Override
	public TestResult addClass(
		String className,
		String testName,
		String searchBy) {
		return null;
	}


	@Override
	public Class<?> getService(
		String fixtureName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFeedableWebPage getPage(
		String fixtureName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUserVariables(
		Map<String, Object> userVariables) {
		this.userVariables = userVariables;
	}

	@Override
	public Map<String, Object> getUserVariables() {
		return userVariables;
	}

	@Override
	public Collection<IFeedableWebPage> getWebPages() {
		// TODO Auto-generated method stub
		return null;
	}
}
