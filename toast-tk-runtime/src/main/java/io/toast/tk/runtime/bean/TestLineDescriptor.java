package io.toast.tk.runtime.bean;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.toast.tk.core.adapter.ActionAdapterKind;
import io.toast.tk.dao.domain.impl.test.block.TestBlock;
import io.toast.tk.dao.domain.impl.test.block.line.TestLine;

public class TestLineDescriptor {

	private static final String KINDS;
	
	private static final String REGEX;
	
	private static final Pattern PATTERN;
	
	static {
		KINDS = ActionAdapterKind.swing.name() + "|" 
				 + ActionAdapterKind.web.name() + "|"
				 + ActionAdapterKind.service.name();
		REGEX = "@(" + KINDS + "):?([\\w\\-\\.]*) ([\\w\\W]+)";
		PATTERN = Pattern.compile(REGEX);
	}

	public final TestLine testLine;

	private final TestBlock testBlock;
	
	private String testLineAction;

	private String testLineFixtureName = "";

	private ActionAdapterKind testLineFixtureKind;

	public TestLineDescriptor(
		final TestBlock testBlock,
		final TestLine testLine
	) {
		this.testLine = testLine;
		this.testBlock = testBlock;
		initServiceKind(testBlock, testLine);
	}

	private void initServiceKind(
		final TestBlock testBlock,
		final TestLine testLine
	) {
		final Matcher matcher = PATTERN.matcher(testLine.getTest());
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

	private void setTestLineFixtureName(final String testLineFixtureName) {
		this.testLineFixtureName = testLineFixtureName == null ? "" : testLineFixtureName;
	}

	public String getTestLineFixtureName() {
		return testLineFixtureName;
	}

	private void setTestLineAction(final String testLineAction) {
		this.testLineAction = testLineAction;
	}

	public String getTestLineAction() {
		return testLineAction;
	}

	public ActionAdapterKind getTestLineFixtureKind() {
		return testLineFixtureKind;
	}

	public void setTestLineFixtureKind(final ActionAdapterKind testLineFixtureKind) {
		this.testLineFixtureKind = testLineFixtureKind;
	}

	public boolean isSynchronizedCommand() {
		return testLineAction.endsWith(" !");
	}

	public boolean isFailFatalCommand() {
		return testLineAction.startsWith("! ");
	}

	public String getActionImpl() {
		return isFailFatalCommand() ? testLineAction.substring(2) : testLineAction; 
	}
	
	@Override
	public int hashCode() {
		int hashcode = Objects.hashCode(testLine) + Objects.hashCode(testBlock);
		return hashcode;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
		      return true;
	    }
	    if (obj == null) {
	      return false;
	    }
		return obj.getClass() == this.getClass() ? Objects.equals(testLine, ((TestLineDescriptor) obj).testLine) && Objects.equals(testBlock, ((TestLineDescriptor) obj).testBlock): false;
	}
}