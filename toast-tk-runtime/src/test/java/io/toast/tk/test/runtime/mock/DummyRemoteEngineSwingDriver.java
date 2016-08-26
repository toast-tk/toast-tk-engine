package io.toast.tk.test.runtime.mock;

import io.toast.tk.core.driver.IRemoteSwingAgentDriver;
import io.toast.tk.core.net.request.IIdRequest;
import io.toast.tk.dao.domain.api.test.ITestResult;

public class DummyRemoteEngineSwingDriver implements IRemoteSwingAgentDriver{
@Override
public void init() {
	// TODO Auto-generated method stub
	
}
@Override
	public void process(IIdRequest request) {
		// TODO Auto-generated method stub
		
	}
@Override
	public ITestResult processAndWaitForValue(IIdRequest requestId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
@Override
	public void start(String host) {
		// TODO Auto-generated method stub
		
	}
@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
@Override
	public boolean waitForExist(String requestId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
