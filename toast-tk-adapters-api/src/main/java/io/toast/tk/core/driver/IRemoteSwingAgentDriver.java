package io.toast.tk.core.driver;

import io.toast.tk.core.net.request.IIdRequest;
import io.toast.tk.dao.domain.api.test.ITestResult;

public interface IRemoteSwingAgentDriver {

	void init();

	void start(final String host);

	void stop();

	void process(final IIdRequest request);

	ITestResult processAndWaitForValue(final IIdRequest requestId) throws Exception;

	boolean waitForExist(final String requestId) throws Exception;
}