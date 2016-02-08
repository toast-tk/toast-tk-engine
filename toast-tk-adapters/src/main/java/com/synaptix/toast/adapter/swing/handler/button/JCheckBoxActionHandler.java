package com.synaptix.toast.adapter.swing.handler.button;

import javax.swing.JCheckBox;

import org.fest.swing.fixture.JCheckBoxFixture;

import com.synaptix.toast.adapter.swing.handler.ISwingwidgetActionHandler;
import com.synaptix.toast.adapter.swing.utils.FestRobotInstance;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.dao.domain.api.test.ITestResult.ResultKind;


public class JCheckBoxActionHandler implements ISwingwidgetActionHandler<JCheckBox, String, CommandRequest>{

	@Override
	public String handle(
		JCheckBox checkbox,
		CommandRequest command) {
		switch(command.action) {
			case CLICK :
				try{
					JCheckBoxFixture bFixture = new JCheckBoxFixture(FestRobotInstance.getRobot(), checkbox);
					bFixture.click();
					return ResultKind.SUCCESS.name();
				}catch(Exception e){
					e.printStackTrace();
					return ResultKind.ERROR.name();
				}
			case GET :
				return String.valueOf(checkbox.isSelected());
			default :
				throw new IllegalArgumentException("Unsupported command for JCheckBox: " + command.action.name());
		}
	}
}
