package com.synaptix.toast.adapter.swing.handler.list;

import javax.swing.JComboBox;

import com.synaptix.toast.adapter.swing.handler.ActionProcessor;
import com.synaptix.toast.adapter.swing.handler.ActionProcessorFactory;
import com.synaptix.toast.core.net.request.CommandRequest;

public class JComboBoxActionProcessorFactory extends ActionProcessorFactory {

	@Override
	public ActionProcessor<JComboBox> getProcessor(
		CommandRequest command) {
		switch(command.action) {
			case SET :
				return new JComboBoxSetActionProcessor();
			case GET :
				return new JComboBoxGetActionProcessor();
			case SELECT :
				return new JComboBoxSelectActionProcessor();
			default :
				return null;
		}
	}
}
