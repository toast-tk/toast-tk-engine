package com.synaptix.toast.adapter.swing.component;

import com.synaptix.toast.adapter.swing.SwingAutoElement;
import com.synaptix.toast.adapter.web.HasClickAction;
import com.synaptix.toast.adapter.web.HasSubItems;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.runtime.ISwingElement;

/**
 * button element
 * 
 * @author skokaina
 * 
 */
public class SwingMenuElement extends SwingAutoElement implements HasClickAction, HasSubItems {

	public SwingMenuElement(
		ISwingElement element,
		IRemoteSwingAgentDriver driver) {
		super(element, driver);
	}

	public SwingMenuElement(
		ISwingElement element) {
		super(element);
	}

	@Override
	public boolean click()
		throws Exception {
		boolean res = exists();
		frontEndDriver.process(new CommandRequest.CommandRequestBuilder(null).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).click().build());
		return res;
	}

	@Override
	public void dbClick() {
	}

	@Override
	public void clickOn(
		String itemName)
		throws Exception {
		exists();
		frontEndDriver.process(new CommandRequest.CommandRequestBuilder(null).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).select(itemName).build());
	}
}
