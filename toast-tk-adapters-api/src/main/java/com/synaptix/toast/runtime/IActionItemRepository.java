package com.synaptix.toast.runtime;

import java.util.Collection;
import java.util.Map;

import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.core.runtime.IFeedableWebPage;

public interface IActionItemRepository {

	IFeedableSwingPage getSwingPage(String entityName);

	Collection<IFeedableSwingPage> getSwingPages();

	Collection<IFeedableWebPage> getWebPages();

	IFeedableWebPage getWebPage(String entityName);

	Map<String, Object> getUserVariables();

	void setUserVariables(Map<String, Object> userVariables);

	void addSwingPage(String fixtureName);
	
	void clear();

	void addWebPage(String fixtureName, IFeedableWebPage webPage);

}
