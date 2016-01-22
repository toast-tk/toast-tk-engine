package com.synaptix.toast.adapter.swing.component;

import java.util.UUID;

import com.synaptix.toast.adapter.swing.SwingAutoElement;
import com.synaptix.toast.adapter.web.HasInputBase;
import com.synaptix.toast.adapter.web.HasStringValue;
import com.synaptix.toast.adapter.web.HasTextInput;
import com.synaptix.toast.adapter.web.HasValueBase;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.runtime.ISwingElement;

public class SwingInputElement extends SwingAutoElement implements HasInputBase<String>, HasValueBase<TestResult> {

	public SwingInputElement(
		ISwingElement element,
		IRemoteSwingAgentDriver driver) {
		super(element, driver);
	}

	public SwingInputElement(
		ISwingElement element) {
		super(element);
	}

	@Override
	public TestResult setInput(
		String e)
		throws Exception {
		exists();
		final String requestId = UUID.randomUUID().toString();
		TestResult res = frontEndDriver.processAndWaitForValue(new CommandRequest.CommandRequestBuilder(requestId).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).sendKeys(e).build());
		return res;
	}

	@Override
	public TestResult getValue()
		throws Exception {
		exists();
		final String requestId = UUID.randomUUID().toString();
		CommandRequest request = buildGetInputValueRequest(
			wrappedElement.getLocator(),
			wrappedElement.getType().name(),
			requestId);
		return frontEndDriver.processAndWaitForValue(request);
	}

	public static CommandRequest buildGetInputValueRequest(
		String locator,
		String type,
		final String requestId) {
		return new CommandRequest.CommandRequestBuilder(requestId).with(locator).ofType(type).getValue().build();
	}

	public void clear()
		throws Exception {
		exists();
		frontEndDriver.process(new CommandRequest.CommandRequestBuilder(null).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).clear().build());
	}
}
