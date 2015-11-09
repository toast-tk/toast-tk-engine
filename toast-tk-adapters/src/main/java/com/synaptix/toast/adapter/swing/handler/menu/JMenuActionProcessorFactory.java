package com.synaptix.toast.adapter.swing.handler.menu;

import javax.swing.JMenu;

import com.synaptix.toast.adapter.swing.handler.ActionProcessor;
import com.synaptix.toast.adapter.swing.handler.ActionProcessorFactory;
import com.synaptix.toast.core.net.request.CommandRequest;

public class JMenuActionProcessorFactory extends ActionProcessorFactory {

	@Override
	public ActionProcessor<JMenu> getProcessor(
		CommandRequest command) {
		switch(command.action) {
			case CLICK :
				return new JMenuClickActionProcessor();
			case SELECT :
				return new JMenuSelectActionProcessor();
			default :
				return null;
		}
	}
}
