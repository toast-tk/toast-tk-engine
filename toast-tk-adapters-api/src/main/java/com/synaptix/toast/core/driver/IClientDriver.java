package com.synaptix.toast.core.driver;

import com.synaptix.toast.core.net.request.IIdRequest;

public interface IClientDriver {

	void process(final IIdRequest request);

	String processAndWaitForValue(final IIdRequest requestId) throws Exception;

	void init();

	void start(final String host);

	boolean waitForExist(final String requestId) throws Exception;

	void stop();
}