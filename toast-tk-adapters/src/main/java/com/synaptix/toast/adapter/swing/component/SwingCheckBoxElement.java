package com.synaptix.toast.adapter.swing.component;

import java.util.UUID;

import com.synaptix.toast.adapter.swing.SwingAutoElement;
import com.synaptix.toast.adapter.web.HasClickAction;
import com.synaptix.toast.adapter.web.HasStringValue;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.runtime.ISwingElement;

public class SwingCheckBoxElement extends SwingAutoElement implements HasClickAction, HasStringValue {

	public SwingCheckBoxElement(
		ISwingElement element,
		IRemoteSwingAgentDriver driver) {
		super(element, driver);
	}

	public SwingCheckBoxElement(
		ISwingElement element) {
		super(element);
	}

	public void select()
		throws Exception {
		exists();
		frontEndDriver.process(new CommandRequest.CommandRequestBuilder(null).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).sendKeys("true").build());
	}

	public void deselect()
		throws Exception {
		exists();
		frontEndDriver.process(new CommandRequest.CommandRequestBuilder(null).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).sendKeys("false").build());
	}

	@Override
	public boolean click()
		throws Exception{
		boolean res = exists();
		frontEndDriver.process(new CommandRequest.CommandRequestBuilder(null).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).click().build());
		return res;
	}

	@Override
	public void dbClick() {
	}

	@Override
	public String getValue()
		throws Exception{
		exists();
		final String requestId = UUID.randomUUID().toString();
		CommandRequest request = new CommandRequest.CommandRequestBuilder(requestId).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).getValue().build();
		return frontEndDriver.processAndWaitForValue(request);
	}
}
