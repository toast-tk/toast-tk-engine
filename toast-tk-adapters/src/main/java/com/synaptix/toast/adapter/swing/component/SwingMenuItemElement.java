package com.synaptix.toast.adapter.swing.component;

import java.util.UUID;

import com.synaptix.toast.adapter.swing.SwingAutoElement;
import com.synaptix.toast.adapter.web.HasClickAction;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.report.TestResult.ResultKind;
import com.synaptix.toast.core.runtime.ISwingElement;

/**
 * menu item element
 * 
 * 
 */
public class SwingMenuItemElement extends SwingAutoElement implements HasClickAction {

	public SwingMenuItemElement(
		ISwingElement element,
		IRemoteSwingAgentDriver driver) {
		super(element, driver);
	}

	public SwingMenuItemElement(
		ISwingElement element) {
		super(element);
	}

	@Override
	public TestResult click()
		throws Exception {
		boolean res = exists();
		final String requestId = UUID.randomUUID().toString();
		TestResult result = frontEndDriver.processAndWaitForValue(new CommandRequest.CommandRequestBuilder(requestId).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).click().build());
		result.setResultKind(res && result.getMessage().equals(ResultKind.SUCCESS.name()) ? ResultKind.SUCCESS : ResultKind.ERROR);
		return result;
	}

	@Override
	public void dbClick() {
	}
}
