package com.synaptix.toast.adapter.swing.component;

import java.util.UUID;

import com.synaptix.toast.adapter.swing.SwingAutoElement;
import com.synaptix.toast.adapter.web.HasTextInput;
import com.synaptix.toast.adapter.web.HasValueBase;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.runtime.ISwingElement;
import com.synaptix.toast.dao.domain.api.test.ITestResult;

public class SwingDateElement extends SwingAutoElement implements HasTextInput, HasValueBase<ITestResult> {

	public SwingDateElement(
		ISwingElement element,
		IRemoteSwingAgentDriver driver) {
		super(element, driver);
	}

	public SwingDateElement(
		ISwingElement element) {
		super(element);
	}

	@Override
	public ITestResult setInput(
		String e)
		throws Exception {
		exists();
		final String requestId = UUID.randomUUID().toString();
		ITestResult res = frontEndDriver.processAndWaitForValue(new CommandRequest.CommandRequestBuilder(requestId)
				.with(wrappedElement.getLocator())
				.ofType(wrappedElement.getType().name()).sendKeys(e).build());
		return res;
	}

	public ITestResult setDateText(
		String e)
		throws Exception {
		exists();
		final String requestId = UUID.randomUUID().toString();
		ITestResult res = frontEndDriver.processAndWaitForValue(new CommandRequest.CommandRequestBuilder(requestId)
			.with(wrappedElement.getLocator())
			.ofType("date_text").sendKeys(e).build());
		return res;
	}

	@Override
	public ITestResult getValue()
		throws Exception {
		exists();
		final String requestId = UUID.randomUUID().toString();
		CommandRequest request = new CommandRequest.CommandRequestBuilder(requestId).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).getValue().build();
		return frontEndDriver.processAndWaitForValue(request);
	}
}
