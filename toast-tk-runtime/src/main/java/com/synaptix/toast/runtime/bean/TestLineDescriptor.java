package com.synaptix.toast.runtime.bean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.TestLine;

public class TestLineDescriptor {

	private static String KINDS = ActionAdapterKind.swing.name() + "|" 
						 + ActionAdapterKind.web.name() + "|"
						 + ActionAdapterKind.service.name();
	
	private static String REGEX = "@(" + KINDS + "):?([\\w\\-\\.]*) ([\\w\\W]+)";

	public final TestLine testLine;

	private String testLineAction;

	private String testLineFixtureName;

	private ActionAdapterKind testLineFixtureKind;

	public TestLineDescriptor(
		TestBlock testBlock,
		TestLine testLine) {
		this.testLine = testLine;
		initServiceKind(testBlock, testLine);
	}

	private void initServiceKind(
		TestBlock testBlock,
		TestLine testLine) {
		Pattern pattern = Pattern.compile(REGEX);
		Matcher matcher = pattern.matcher(testLine.getTest());
		if(matcher.find()) {
			setTestLineFixtureKind(ActionAdapterKind.valueOf(matcher.group(1)));
			setTestLineFixtureName(matcher.group(2));
			setTestLineAction(matcher.group(3));
		}
		else {
			setTestLineFixtureKind(ActionAdapterKind.valueOf(testBlock.getFixtureName())); 
			setTestLineAction(testLine.getTest());
		}
	}

	private void setTestLineFixtureName(
		String testLineFixtureName) {
		this.testLineFixtureName = testLineFixtureName;
	}

	public String getTestLineFixtureName() {
		return testLineFixtureName == null ? "" : testLineFixtureName;
	}

	private void setTestLineAction(
		String testLineAction) {
		this.testLineAction = testLineAction;
	}

	public String getTestLineAction() {
		return testLineAction;
	}

	public ActionAdapterKind getTestLineFixtureKind() {
		return testLineFixtureKind;
	}

	public void setTestLineFixtureKind(
		ActionAdapterKind testLineFixtureKind) {
		this.testLineFixtureKind = testLineFixtureKind;
	}

	public boolean isSynchronizedCommand() {
		return testLineAction.endsWith(" !");
	}

	public boolean isFailFatalCommand() {
		return testLineAction.startsWith("* ");
	}

	public String getActionImpl() {
		String actionImpl = testLineAction;
		if(isFailFatalCommand()) {
			actionImpl = actionImpl.substring(2);
		}
		return actionImpl;
	}
}