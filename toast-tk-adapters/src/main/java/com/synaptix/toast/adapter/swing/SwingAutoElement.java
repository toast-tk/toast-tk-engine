package com.synaptix.toast.adapter.swing;

import java.util.UUID;

import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.runtime.ISwingElement;

public abstract class SwingAutoElement {

	protected ISwingElement wrappedElement;

	protected IRemoteSwingAgentDriver frontEndDriver;

	protected void setWrappedElement(
		ISwingElement wrappedElement) {
		this.wrappedElement = wrappedElement;
	}

	protected IRemoteSwingAgentDriver getFrontEndDriver() {
		return frontEndDriver;
	}

	protected void setFrontEndDriver(
		IRemoteSwingAgentDriver frontEndDriver) {
		this.frontEndDriver = frontEndDriver;
	}

	public SwingAutoElement(
		ISwingElement element,
		IRemoteSwingAgentDriver driver) {
		this.wrappedElement = element;
		this.frontEndDriver = driver;
	}

	public SwingAutoElement(
		ISwingElement element) {
		this.wrappedElement = element;
	}

	public SwingAutoElement() {
	}

	public boolean exists()
		throws Exception {
		final String requestId = UUID.randomUUID().toString();
		final CommandRequest command = new CommandRequest.CommandRequestBuilder(requestId)
			.with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name())
			.exists()
			.build();
		frontEndDriver.process(command);
		return frontEndDriver.waitForExist(requestId);
	}

	public ISwingElement getWrappedElement() {
		return wrappedElement;
	}
}
