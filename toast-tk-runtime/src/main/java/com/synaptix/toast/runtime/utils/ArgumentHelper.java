package com.synaptix.toast.runtime.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.synaptix.toast.runtime.ActionItemDescriptionCollector;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ActionItem;
import com.synaptix.toast.runtime.bean.ActionItem.ActionCategoryEnum;
import com.synaptix.toast.runtime.bean.ActionItem.ActionTypeEnum;
import com.synaptix.toast.runtime.constant.Property;

public class ArgumentHelper {
	
	private static final List<ActionItem> ACTION_ITEMS = ActionItemDescriptionCollector.initActionItems();
	
	public static String convertActionSentenceToRegex(
		String actionSentence) {
		String convertedSentence = actionSentence;
		Pattern regexPattern = Pattern.compile(Property.ACTION_ITEM_REGEX);
		Matcher matcher = regexPattern.matcher(actionSentence);
		while(matcher.find()) {
			String actionItemDefinition = matcher.group(1);
			String[] groupArray = actionItemDefinition.split(":");
			String regex = null;
			if(hasOnlyDeclaredActionItemCategory(groupArray)) {
				String category = groupArray[0];
				ActionCategoryEnum categoryEnum = ActionItem.ActionCategoryEnum.valueOf(category);
				regex = getActionItemRegex(categoryEnum, ActionItem.ActionTypeEnum.string);
			}
			else if(hasDeclaredCategoryAndType(groupArray)) {
				String category = groupArray[0];
				ActionCategoryEnum categoryEnum = ActionItem.ActionCategoryEnum.valueOf(category);
				String type = groupArray[1];
				ActionTypeEnum typeEnum = ActionItem.ActionTypeEnum.valueOf(type);
				regex = getActionItemRegex(categoryEnum, typeEnum);
			}
			else if(hadDeclaredAll(groupArray)) {
				String category = groupArray[1];
				ActionCategoryEnum categoryEnum = ActionItem.ActionCategoryEnum.valueOf(category);
				String type = groupArray[2];
				ActionTypeEnum typeEnum = ActionItem.ActionTypeEnum.valueOf(type);
				regex = getActionItemRegex(categoryEnum, typeEnum);
			}
			if(regex != null) {
				convertedSentence = matcher.replaceFirst(regex.replace("\\", "\\\\\\"));
				matcher = regexPattern.matcher(convertedSentence);
			}
		}
		return convertedSentence;
	}

	private static String getActionItemRegex(
		ActionCategoryEnum categoryEnum,
		ActionTypeEnum typeEnum) {
		for(ActionItem actionItem : ACTION_ITEMS) {
			if(actionItem.category.equals(categoryEnum)) {
				if(actionItem.kind.equals(typeEnum)) {
					return actionItem.regex;
				}
			}
		}
		return null;
	}
	
	private static boolean hadDeclaredAll(
		String[] groupArray) {
		return groupArray.length == 3;
	}

	private static boolean hasDeclaredCategoryAndType(
		String[] groupArray) {
		return groupArray.length == 2;
	}

	private static boolean hasOnlyDeclaredActionItemCategory(
		String[] groupArray) {
		return groupArray.length == 1;
	}
	
	public static Object buildActionAdapterArgument(
		IActionItemRepository repoSetup,
		String group) {
		group = group.replaceAll("\\*", "");
		if(repoSetup.getUserVariables().containsKey(group)){
			if(isInputVariable(group)) {
				Object object = repoSetup.getUserVariables().get(group);
				if(object != null && object instanceof String) {
					String value = (String) object;
					value = handleValueWithNestedVars(repoSetup, value);
					object = value;
				}
				return object;
			}
		}
		return group;
	}

	private static String handleValueWithNestedVars(
		IActionItemRepository repoSetup,
		String value) {
		Pattern p = Pattern.compile(Property.ACTION_ITEM_VAR_REGEX, Pattern.MULTILINE);
		Matcher m = p.matcher(value);
		int pos = 0;
		while(m.find()) {
			String varName = m.group(pos + 1);
			if(repoSetup.getUserVariables().containsKey(varName)) {
				Object varValue = repoSetup.getUserVariables().get(varName);
				value = value.replaceFirst("\\" + varName + "\\b", (String) varValue);
			}
		}
		return value;
	}

	private static boolean isInputVariable(
		String group) {
		return group.startsWith("$")
			&& !group.substring(1).contains("$")
			&& !group.substring(1).contains(Property.DEFAULT_PARAM_SEPARATOR);
	}
	
}
