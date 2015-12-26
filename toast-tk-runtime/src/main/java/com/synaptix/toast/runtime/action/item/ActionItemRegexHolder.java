package com.synaptix.toast.runtime.action.item;

import java.util.regex.Pattern;

public class ActionItemRegexHolder {
	private static final String ACTION_ITEM_REGEX = "\\{\\{([\\.\\w:]+)\\}\\}";
	private static final String FULL_ACTION_ITEM_REGEX = "(\\{\\{[\\.\\w:]+\\}\\})";
	private static final String VAR_REGEX = "\\*(\\$\\w+)\\*";
	
	public static final Pattern getVarPattern(){
		return Pattern.compile(VAR_REGEX);
	}
	
	public static final Pattern getMetaPattern(){
		return Pattern.compile(ACTION_ITEM_REGEX);
	}
	
	public static final Pattern getFullMetaPattern(){
		return Pattern.compile(FULL_ACTION_ITEM_REGEX);
	}
	
}
