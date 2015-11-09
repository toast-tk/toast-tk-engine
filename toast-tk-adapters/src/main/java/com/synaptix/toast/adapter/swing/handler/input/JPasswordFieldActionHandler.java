package com.synaptix.toast.adapter.swing.handler.input;

import javax.swing.JPasswordField;

import org.apache.commons.lang3.StringUtils;

import com.synaptix.toast.adapter.swing.handler.ISwingwidgetActionHandler;
import com.synaptix.toast.adapter.swing.utils.FestRobotInstance;
import com.synaptix.toast.core.net.request.CommandRequest;


public class JPasswordFieldActionHandler implements ISwingwidgetActionHandler<JPasswordField, String, CommandRequest>{

	@Override
	public String handle(
		JPasswordField textField,
		CommandRequest command) {
		switch(command.action) {
			case SET :
				textField.setText(command.value);
				break;
			case CLICK :
				FestRobotInstance.getRobot().click(textField);
				break;
			case GET :
				return StringUtils.join(textField.getPassword(), "");
			default :
				throw new IllegalArgumentException("Unsupported command for JPasswordField: " + command.action.name());
		}
		return null;
	}
}
