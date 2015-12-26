package com.synaptix.toast.runtime.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.runtime.ActionItemDescriptionCollector;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.action.item.ActionItemRegexHolder;
import com.synaptix.toast.runtime.bean.ActionItem;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;
import com.synaptix.toast.runtime.bean.ActionItem.ActionCategoryEnum;
import com.synaptix.toast.runtime.bean.ActionItem.ActionTypeEnum;
import com.synaptix.toast.runtime.bean.CommandArgumentDescriptor;
import com.synaptix.toast.runtime.constant.Property;

public class ArgumentHelper {
	
	private static final List<ActionItem> ACTION_ITEMS = ActionItemDescriptionCollector.initActionItems();
	
	public static CommandArgumentDescriptor convertActionSentenceToRegex(
		Action action) {
		return convertActionSentenceToRegex(action.action());
	}
	
	public static CommandArgumentDescriptor convertActionSentenceToRegex(String actionSentence
			) {
		CommandArgumentDescriptor descriptor = new CommandArgumentDescriptor();
		List<ArgumentDescriptor> arguments = new ArrayList<>();
		String convertedSentence = actionSentence;
		Pattern regexPattern = ActionItemRegexHolder.getMetaPattern();
		Matcher matcher = regexPattern.matcher(actionSentence);
		while(matcher.find()) {
			String actionItemDefinition = matcher.group(1);
			String[] groupArray = actionItemDefinition.split(":");
			String regex = null;
			if(hasOnlyDeclaredActionItemCategory(groupArray)) {
				regex = buildActionItemRegex_1arg(arguments, groupArray);
			}
			else if(hasDeclaredCategoryAndType(groupArray)) {
				regex = buildActionItemRegex_2args(arguments, groupArray);
			}
			else if(hadDeclaredAll(groupArray)) {
				regex = buildActionItemRegex_3args(arguments, groupArray);
			}
			if(regex != null) {
				convertedSentence = matcher.replaceFirst(regex.replace("\\", "\\\\\\"));
				matcher = regexPattern.matcher(convertedSentence);
			}
		}
		descriptor.regex = convertedSentence;
		descriptor.arguments = arguments;
		return descriptor;
	}

	private static String buildActionItemRegex_3args(List<ArgumentDescriptor> arguments, String[] groupArray) {
		String regex;
		String category = groupArray[1];
		ActionCategoryEnum categoryEnum = ActionItem.ActionCategoryEnum.valueOf(category);
		String type = groupArray[2];
		ActionTypeEnum typeEnum = ActionItem.ActionTypeEnum.valueOf(type);
		
		ArgumentDescriptor argDescriptor = new ArgumentDescriptor();
		argDescriptor.name = groupArray[0];
		argDescriptor.categoryEnum = categoryEnum;
		argDescriptor.typeEnum = typeEnum;	
		arguments.add(argDescriptor);
		
		regex = getActionItemRegex(categoryEnum, typeEnum);
		return regex;
	}

	private static String buildActionItemRegex_2args(List<ArgumentDescriptor> arguments, String[] groupArray) {
		String regex;
		String category = groupArray[0];
		ActionCategoryEnum categoryEnum = ActionItem.ActionCategoryEnum.valueOf(category);
		String type = groupArray[1];
		ActionTypeEnum typeEnum = ActionItem.ActionTypeEnum.valueOf(type);
		ArgumentDescriptor argDescriptor = new ArgumentDescriptor();
		argDescriptor.categoryEnum = categoryEnum;
		argDescriptor.typeEnum = typeEnum;
		arguments.add(argDescriptor);
		regex = getActionItemRegex(categoryEnum, typeEnum);
		return regex;
	}

	private static String buildActionItemRegex_1arg(List<ArgumentDescriptor> arguments, String[] groupArray) {
		String regex;
		String category = groupArray[0];
		ActionCategoryEnum categoryEnum = ActionItem.ActionCategoryEnum.valueOf(category);
		ArgumentDescriptor argDescriptor = new ArgumentDescriptor();
		argDescriptor.categoryEnum = categoryEnum;
		argDescriptor.typeEnum = ActionItem.ActionTypeEnum.string;
		arguments.add(argDescriptor);
		regex = getActionItemRegex(categoryEnum, ActionItem.ActionTypeEnum.string);
		return regex;
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
			Object object = repoSetup.getUserVariables().get(group);
			if(!(object instanceof String)){
				return object;
			}
			if(isInputVariable(group)) {
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
