package com.synaptix.toast.adapter.swing.handler;

import com.synaptix.toast.core.net.request.CommandRequest;

public abstract class ActionProcessorFactory {

	public abstract ActionProcessor getProcessor(
		CommandRequest command);
}
