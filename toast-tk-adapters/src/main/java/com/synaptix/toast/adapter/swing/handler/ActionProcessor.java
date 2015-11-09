package com.synaptix.toast.adapter.swing.handler;

import java.awt.Component;

import com.synaptix.toast.core.net.request.CommandRequest;

public interface ActionProcessor<C extends Component> {

	public String processCommandOnComponent(
		CommandRequest command,
		C target);
}
