package com.synaptix.toast.runtime.action.item;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import com.google.gson.Gson;
import com.google.inject.Injector;
import com.synaptix.toast.adapter.web.component.DefaultWebPage;
import com.synaptix.toast.adapter.web.component.WebAutoElement;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;

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
	public Object handle(String group, String argValue) throws Exception {
		String[] components = StringUtils.split(group, ".");
		return getPageField(components[0], components[1]);
	}

	@Override
	public void setArgumentDescriptor(ArgumentDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	private WebAutoElement getPageField(String pageName, String fieldName) {
		DefaultWebPage page = (DefaultWebPage) objectRepository.getPage(pageName);
		WebAutoElement autoElement = page.getAutoElement(fieldName);
		return autoElement;
	}

}
