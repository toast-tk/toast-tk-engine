package com.synaptix.toast.adapter.swing.handler.input;

import javax.swing.JLabel;

import com.synaptix.toast.adapter.swing.handler.ISwingwidgetActionHandler;
import com.synaptix.toast.core.net.request.CommandRequest;


public class JLabelActionHandler implements ISwingwidgetActionHandler<JLabel, String, CommandRequest>{

	@Override
	public String handle(
		JLabel component,
		CommandRequest command) {
		switch(command.action) {
			case SET :
				component.setText(command.value);
				break;
			case GET :
				return component.getText();
			default :
				throw new IllegalArgumentException("Unsupported command for JLabel: " + command.action.name());
		}
		return null;
	}
}
