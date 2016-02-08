package com.synaptix.toast.core.driver;

import com.synaptix.toast.core.net.request.IIdRequest;
import com.synaptix.toast.dao.domain.api.test.ITestResult;

public interface IRemoteSwingAgentDriver {

	public void init();

	public void start(
		String host);

	public void stop();

	public void process(
		IIdRequest request);

	public ITestResult processAndWaitForValue(
		IIdRequest requestId)
		throws Exception;

	public boolean waitForExist(
		String requestId)
		throws Exception;
}
