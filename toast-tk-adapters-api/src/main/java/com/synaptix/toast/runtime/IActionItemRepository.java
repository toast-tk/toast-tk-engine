package com.synaptix.toast.runtime;

import java.util.Collection;
import java.util.Map;

import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.core.runtime.IWebAutoElement;

public interface IActionItemRepository {

	IFeedableSwingPage getSwingPage(final String entityName);

	Collection<IFeedableSwingPage> getSwingPages();

	Collection<IFeedableWebPage> getWebPages();

	IFeedableWebPage getWebPage(final String entityName);

	Map<String, Object> getUserVariables();

	void setUserVariables(final Map<String, Object> userVariables);
	
	Map<String, IWebAutoElement<?>> getWebComponents();

	void setWebComponents(final Map<String, IWebAutoElement<?>> webComponents);

	void addSwingPage(final String fixtureName);
	
	void clear();

	void addWebPage(final String fixtureName, final IFeedableWebPage webPage);

}