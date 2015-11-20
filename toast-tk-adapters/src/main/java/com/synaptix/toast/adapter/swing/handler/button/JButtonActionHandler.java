package com.synaptix.toast.adapter.swing.handler.button;

import javax.swing.JButton;

import org.fest.swing.fixture.JButtonFixture;

import com.synaptix.toast.adapter.swing.handler.ISwingwidgetActionHandler;
import com.synaptix.toast.adapter.swing.utils.FestRobotInstance;
import com.synaptix.toast.core.net.request.CommandRequest;


public class JButtonActionHandler implements ISwingwidgetActionHandler<JButton, String, CommandRequest>{

	@Override
	public String handle(
		final JButton button,
		CommandRequest command) {
		switch(command.action) {
			case CLICK :
				FestRobotInstance.runOutsideEDT(new Runnable() {
					@Override
					public void run() {
						JButtonFixture fixture = new JButtonFixture(FestRobotInstance.getRobot(), button);
						fixture.click();
					}
				});
				break;
			default :
				throw new IllegalArgumentException("Unsupported command for JButton: " + command.action.name());
		}
		return null;
	}
}
