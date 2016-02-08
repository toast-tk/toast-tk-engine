package com.synaptix.toast.adapter.swing.component;

import java.util.UUID;

import com.synaptix.toast.adapter.swing.SwingAutoElement;
import com.synaptix.toast.adapter.web.HasClickAction;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.runtime.ISwingElement;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult.ResultKind;

public class SwingButtonElement extends SwingAutoElement implements HasClickAction {

	public SwingButtonElement(
		ISwingElement element,
		IRemoteSwingAgentDriver driver) {
		super(element, driver);
	}

	public SwingButtonElement(
		ISwingElement element) {
		super(element);
	}

	@Override
	public ITestResult click()
		throws Exception {
		boolean res = exists();
		final String requestId = UUID.randomUUID().toString();
		ITestResult result = frontEndDriver.processAndWaitForValue(new CommandRequest.CommandRequestBuilder(requestId).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).click().build());
		result.setResultKind(res && result.getMessage().equals(ResultKind.SUCCESS.name()) ? ResultKind.SUCCESS : ResultKind.ERROR);
		return result;
	}

	public static CommandRequest buildClickRequest(
		String locator,
		String type,
		final String requestId) {
		return new CommandRequest.CommandRequestBuilder(requestId).with(locator).ofType(type).click().build();
	}
	
	@Override
	public void dbClick() {
	}
}
