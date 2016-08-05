package io.toast.tk.core.event;

import io.toast.tk.dao.domain.api.test.ITestResult;
public class TestProgressMessage {
	
	private ITestResult result;
	
	protected TestProgressMessage() {
		
	}
	
	public TestProgressMessage(ITestResult result) {
		this.result = result;
	}
	

	public ITestResult getMsg() {
		return result;
	}

}