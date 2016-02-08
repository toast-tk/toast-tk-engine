package com.synaptix.toast.test.runtime.mock;

import java.util.Collection;
import java.util.Map;

import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.core.runtime.IWebAutoElement;
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

	@Override
	public IFeedableWebPage getWebPage(String entityName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWebPage(String fixtureName, IFeedableWebPage webPage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, IWebAutoElement<?>> getWebComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWebComponents(Map<String, IWebAutoElement<?>> webComponents) {
		// TODO Auto-generated method stub
		
	}

}
