package com.synaptix.toast.runtime.report;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.synaptix.toast.core.event.TestProgressMessage;
import com.synaptix.toast.runtime.IReportUpdateCallBack;

public class DefaultTestProgressReporter {

	private IReportUpdateCallBack reportUpdateCallBack;

	public DefaultTestProgressReporter(
		final EventBus bus
	) {
		bus.register(this);
	}
	
	public void setReportCallBack(final IReportUpdateCallBack callback){
		this.reportUpdateCallBack = callback;
	}
	
	@Subscribe
	public void handleTestExecutionProgress(final TestProgressMessage progressMessage){
		this.reportUpdateCallBack.onUpdate(progressMessage.getMsg());
	}

}