package com.synaptix.toast.runtime.action.item;

import org.apache.commons.collections4.iterators.ArrayIterator;
import org.apache.commons.lang3.StringUtils;

import com.google.inject.Injector;
import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.core.runtime.IWebAutoElement;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;


/**
 * Must be refactored, has currently 2 responsibilites:
 * 	- update the container locator
 *  - provide a webcomponent for a given component reference
 */
public class WebValueHandler implements IValueHandler{

	private Injector injector;
	private ArgumentDescriptor descriptor;
	private IActionItemRepository objectRepository;

	@Override
	public void setInjector(Injector injector) {
		this.injector = injector;
		this.objectRepository = injector.getInstance(IActionItemRepository.class);
	}

	@Override
	public Object handle(String componentReference, String argValue) throws Exception {
		String[] components = StringUtils.split(componentReference, ".");
		if(components.length <= 1){
			throw new IllegalAccessException("Web value is invalid: " + componentReference);
		}
		else if (components.length == 2){
			return getPageField(components[0], components[1]);
		}else{
			ArrayIterator<String> componentIterator = new ArrayIterator<String>(components, 1, components.length - 1);
			String containerName = components[0];
			String pageName = null;
			while(componentIterator.hasNext()){
				pageName = componentIterator.next();
				if(pageName.contains(":")){
					IWebAutoElement<?> autoElement = getPageField(containerName, pageName);
					pageName = StringUtils.split(pageName, ":")[0];
					IFeedableWebPage webPage = (IFeedableWebPage) objectRepository.getPage(pageName);
					webPage.setLocator(autoElement.getWrappedElement());
					containerName = pageName;
				}
			}
			return getPageField(pageName, components[components.length - 1]);
		}
	}

	@Override
	public void setArgumentDescriptor(ArgumentDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	private IWebAutoElement<?> getPageField(String pageName, String fieldName) {
		IFeedableWebPage page = (IFeedableWebPage) objectRepository.getPage(pageName);
		if (page == null) {
			return null;
		}
		return page.getAutoElement(fieldName);
	}

}
