package com.synaptix.toast.adapter.swing.handler.menu;

import javax.swing.JMenu;

import org.fest.swing.core.MouseButton;
import org.fest.swing.core.Robot;
import org.fest.swing.fixture.JMenuItemFixture;
import org.fest.swing.fixture.JPopupMenuFixture;

import com.synaptix.toast.adapter.swing.handler.ActionProcessor;
import com.synaptix.toast.adapter.swing.utils.FestRobotInstance;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.report.TestResult.ResultKind;

class JMenuSelectActionProcessor implements ActionProcessor<JMenu> {

	@Override
	public String processCommandOnComponent(
		CommandRequest command,
		JMenu target) {
		Robot robot = FestRobotInstance.getRobot();
		if(target == null) {
			robot.pressMouse(MouseButton.RIGHT_BUTTON);
		}
		else {
			robot.click(target);
		}
		JPopupMenuFixture popupFixture = new JPopupMenuFixture(robot, robot.findActivePopupMenu());
		JMenuItemFixture menuItemWithPath = popupFixture.menuItemWithPath(command.value);
		if(menuItemWithPath != null && menuItemWithPath.component().isEnabled()) {
			menuItemWithPath.click();
			return ResultKind.SUCCESS.name();
		}
		else {
			return ResultKind.FAILURE.name();
		}
	}
}
