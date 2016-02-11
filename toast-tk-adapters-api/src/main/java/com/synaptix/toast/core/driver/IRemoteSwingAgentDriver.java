package com.synaptix.toast.core.driver;

import com.synaptix.toast.core.net.request.IIdRequest;
import com.synaptix.toast.dao.domain.api.test.ITestResult;

public interface IRemoteSwingAgentDriver {

	void init();

	void start(final String host);

	void stop();

	void process(final IIdRequest request);

	ITestResult processAndWaitForValue(final IIdRequest requestId) throws Exception;

	boolean waitForExist(final String requestId) throws Exception;
}