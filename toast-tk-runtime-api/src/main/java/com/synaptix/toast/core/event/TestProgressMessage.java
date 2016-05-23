package com.synaptix.toast.core.event;

import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;

public class TestProgressMessage {
	
	private ITestPage page;

	private String msg;
	
	protected TestProgressMessage() {
		
	}
	
	public TestProgressMessage(String msg) {
		this.msg = msg;
	}
	
	
	public TestProgressMessage(final ITestPage page){
		this.setPage(page);
	}

	public ITestPage getPage() {
		return page;
	}

	private void setPage(final ITestPage page) {
		this.page = page;
	}

	public String getMsg() {
		return msg;
	}

}