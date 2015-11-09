package com.synaptix.toast.adapter.swing.handler.input;

import javax.swing.JTextArea;

import org.fest.swing.fixture.JTextComponentFixture;

import com.synaptix.toast.adapter.swing.handler.ISwingwidgetActionHandler;
import com.synaptix.toast.adapter.swing.utils.FestRobotInstance;
import com.synaptix.toast.core.net.request.CommandRequest;


public class JTextAreaActionHandler implements ISwingwidgetActionHandler<JTextArea, String, CommandRequest>{

	@Override
	public String handle(
		JTextArea component,
		CommandRequest command) {
		JTextComponentFixture tFixture = new JTextComponentFixture(FestRobotInstance.getRobot(), component);
		switch(command.action) {
			case SET :
				component.setText(command.value);
				component.revalidate();
				break;
			case GET :
				return component.getText();
			case CLICK :
				FestRobotInstance.getRobot().click(component);
				break;
			case CLEAR :
				tFixture.setText("");
			default :
				throw new IllegalArgumentException("Unsupported command for JTextArea: " + command.action.name());
		}
		return null;
	}
}
