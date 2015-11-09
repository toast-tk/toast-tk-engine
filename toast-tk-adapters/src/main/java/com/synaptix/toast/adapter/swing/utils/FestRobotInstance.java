package com.synaptix.toast.adapter.swing.utils;

import java.awt.Point;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.MouseButton;
import org.fest.swing.core.Robot;

public final class FestRobotInstance {

	private static class FestRobotHolder {

		static final FestRobotInstance INSTANCE = new FestRobotInstance();
	}

	public static Robot getRobot() {
		return FestRobotHolder.INSTANCE.rbt;
	}

	private final Robot rbt;

	FestRobotInstance() {
		this.rbt = BasicRobot.robotWithCurrentAwtHierarchy();
		rbt.cleanUpWithoutDisposingWindows();
	}
	
	public static void runOutsideEDT(Runnable runnable){
		new Thread(runnable).start();
	}

	public void doubleClick(
		final Point where) {
		rbt.click(where, MouseButton.LEFT_BUTTON, 2);
	}

	public void rightClick(
		final Point where) {
		rbt.click(where, MouseButton.RIGHT_BUTTON, 1);
	}

	public void leftClick(
		final Point where) {
		rbt.click(where, MouseButton.LEFT_BUTTON, 1);
	}
}