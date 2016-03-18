package com.synaptix.toast.runtime.action.item;

import java.util.regex.Pattern;

public class ActionItemRegexHolder {

	private static final String ACTION_ITEM_REGEX = "\\{\\{([\\.\\w:]+)\\}\\}";

	private static final String FULL_ACTION_ITEM_REGEX = "(\\{\\{[\\.\\w:]+\\}\\})";

	private static final String VAR_REGEX = "\\*(\\$\\w+)\\*";

	private static final Pattern VAR_REGEX_PATTERN = Pattern.compile(VAR_REGEX);
	
	private static final Pattern ACTION_ITEM_PATTERN = Pattern.compile(ACTION_ITEM_REGEX);
	
	private static final Pattern FULL_ACTION_PATTERN = Pattern.compile(FULL_ACTION_ITEM_REGEX);
	
	public static final Pattern getVarPattern() {
		return VAR_REGEX_PATTERN;
	}

	public static final Pattern getMetaPattern() {
		return ACTION_ITEM_PATTERN;
	}

	public static final Pattern getFullMetaPattern() {
		return FULL_ACTION_PATTERN;
	}
}