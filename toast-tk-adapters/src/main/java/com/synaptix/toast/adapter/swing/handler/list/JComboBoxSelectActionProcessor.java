package com.synaptix.toast.adapter.swing.handler.list;

import javax.swing.JComboBox;

import org.apache.commons.lang3.StringUtils;
import org.fest.swing.fixture.JComboBoxFixture;

import com.synaptix.toast.adapter.swing.handler.ActionProcessor;
import com.synaptix.toast.adapter.swing.utils.FestRobotInstance;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.report.TestResult;

class JComboBoxSelectActionProcessor implements ActionProcessor<JComboBox> {

	@Override
	public String processCommandOnComponent(
		CommandRequest command,
		JComboBox target) {
		JComboBoxFixture fixture = new JComboBoxFixture(FestRobotInstance.getRobot(), target);
		if(StringUtils.isNumeric(command.value)) {
			return handleNumericIndex(command, fixture);
		}
		else {
			return handleIndexByValue(command, fixture);
		}
	}

	private String handleIndexByValue(
		CommandRequest command,
		JComboBoxFixture fixture) {
		fixture.selectItem(command.value);
		int selectedIndex = fixture.component().getSelectedIndex();
		if(command.value.equalsIgnoreCase(fixture.valueAt(selectedIndex))) {
			return TestResult.ResultKind.SUCCESS.name();
		}
		else {
			return TestResult.ResultKind.ERROR.name();
		}
	}

	private String handleNumericIndex(
		CommandRequest command,
		JComboBoxFixture fixture) {
		int indexToSelect = Integer.parseInt(command.value);
		boolean isInBoundIndex = indexToSelect >= 0 && indexToSelect < fixture.component().getItemCount();
		if(isInBoundIndex) {
			fixture.selectItem(indexToSelect);
			return TestResult.ResultKind.SUCCESS.name();
		}
		else {
			return TestResult.ResultKind.ERROR.name();
		}
	}
}
