package io.toast.tk.runtime.report;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import io.toast.tk.core.event.TestProgressMessage;
import io.toast.tk.runtime.IReportUpdateCallBack;

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
		if(this.reportUpdateCallBack != null){
			this.reportUpdateCallBack.onUpdate(progressMessage.getMsg());
		}
	}

}