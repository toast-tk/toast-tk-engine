package com.synaptix.toast.adapter.swing.handler.menu;

import javax.swing.JMenu;

import org.fest.swing.core.Robot;

import com.synaptix.toast.adapter.swing.handler.ActionProcessor;
import com.synaptix.toast.adapter.swing.utils.FestRobotInstance;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.report.TestResult.ResultKind;

/**
 * Click on a JMenu 
 * Select a sub menu item from there.
 *
 */
class JMenuClickActionProcessor implements ActionProcessor<JMenu> {

	@Override
	public String processCommandOnComponent(
		CommandRequest command,
		JMenu target) {
		Robot robot = FestRobotInstance.getRobot();
		robot.click(target);
		return ResultKind.SUCCESS.name();
	}
}
