package com.synaptix.toast.adapter.swing.handler.button;

import javax.swing.JCheckBox;

import org.fest.swing.fixture.JCheckBoxFixture;

import com.synaptix.toast.adapter.swing.handler.ISwingwidgetActionHandler;
import com.synaptix.toast.adapter.swing.utils.FestRobotInstance;
import com.synaptix.toast.core.net.request.CommandRequest;


public class JCheckBoxActionHandler implements ISwingwidgetActionHandler<JCheckBox, String, CommandRequest>{

	@Override
	public String handle(
		JCheckBox checkbox,
		CommandRequest command) {
		switch(command.action) {
			case CLICK :
				JCheckBoxFixture bFixture = new JCheckBoxFixture(FestRobotInstance.getRobot(), checkbox);
				bFixture.click();
				break;
			case GET :
				return String.valueOf(checkbox.isSelected());
			default :
				throw new IllegalArgumentException("Unsupported command for JCheckBox: " + command.action.name());
		}
		return null;
	}
}
