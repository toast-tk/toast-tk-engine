package com.synaptix.toast.runtime;

import java.util.Collection;
import java.util.Map;

import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.core.runtime.IFeedableWebPage;

public interface IActionItemRepository {

	public IFeedableSwingPage getSwingPage(String entityName);

	public Collection<IFeedableSwingPage> getSwingPages();

	public Collection<IFeedableWebPage> getWebPages();

	public IFeedableWebPage getWebPage(String entityName);

	public Map<String, Object> getUserVariables();

	public void setUserVariables(Map<String, Object> userVariables);

	public void addSwingPage(String fixtureName);
	
	public void clear();

	public 	void addWebPage(String fixtureName, IFeedableWebPage webPage);

}
