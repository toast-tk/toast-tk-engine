package com.synaptix.toast.adapter.swing.handler.list;

import javax.swing.JComboBox;

import org.fest.swing.fixture.JComboBoxFixture;

import com.synaptix.toast.adapter.swing.handler.ActionProcessor;
import com.synaptix.toast.adapter.swing.utils.FestRobotInstance;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.dao.domain.api.test.ITestResult;

class JComboBoxSetActionProcessor implements ActionProcessor<JComboBox> {

	@Override
	public String processCommandOnComponent(
		CommandRequest command,
		JComboBox target) {
		JComboBoxFixture fixture = new JComboBoxFixture(FestRobotInstance.getRobot(), target);
		fixture.focus().enterText(command.value);
		int selectedIndex = fixture.component().getSelectedIndex();
		if(command.value.equalsIgnoreCase(fixture.valueAt(selectedIndex))) {
			return ITestResult.ResultKind.SUCCESS.name();
		}
		else {
			return ITestResult.ResultKind.ERROR.name();
		}
	}
}
