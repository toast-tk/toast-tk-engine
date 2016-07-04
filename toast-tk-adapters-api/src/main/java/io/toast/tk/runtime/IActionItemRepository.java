package io.toast.tk.runtime;

import java.util.Collection;
import java.util.Map;

import io.toast.tk.core.runtime.IFeedableSwingPage;
import io.toast.tk.core.runtime.IFeedableWebPage;
import io.toast.tk.core.runtime.IWebAutoElement;

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