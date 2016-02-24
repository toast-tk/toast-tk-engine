package com.synaptix.toast.runtime.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
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
	
	public static CommandArgumentDescriptor convertActionSentenceToRegex(final Action action) {
		return convertActionSentenceToRegex(action.action());
	}
	
	public static CommandArgumentDescriptor convertActionSentenceToRegex(final String actionSentence) {
		final List<ArgumentDescriptor> arguments = new ArrayList<>();
		String convertedSentence = actionSentence;
		final Pattern regexPattern = ActionItemRegexHolder.getMetaPattern();
		Matcher matcher = regexPattern.matcher(actionSentence);
		while(matcher.find()) {
			final String actionItemDefinition = matcher.group(1);
			final String[] groupArray = actionItemDefinition.split(":");
			final String regex = findRegex(arguments, groupArray);
			if(regex != null) {
				convertedSentence = matcher.replaceFirst(regex.replace("\\", "\\\\\\"));
				matcher = regexPattern.matcher(convertedSentence);
			}
		}
		return new CommandArgumentDescriptor(convertedSentence, arguments);
	}

	private static String findRegex(
		final List<ArgumentDescriptor> arguments,
		final String[] groupArray 
	) {
		if(hasOnlyDeclaredActionItemCategory(groupArray)) {
			return buildActionItemRegex_1arg(arguments, groupArray);
		}
		else if(hasDeclaredCategoryAndType(groupArray)) {
			return buildActionItemRegex_2args(arguments, groupArray);
		}
		else if(hadDeclaredAll(groupArray)) {
			return buildActionItemRegex_3args(arguments, groupArray);
		}
		return null;
	}

	private static String buildActionItemRegex_3args(List<ArgumentDescriptor> arguments, String[] groupArray) {
		final String category = groupArray[1];
		final ActionCategoryEnum categoryEnum = ActionItem.ActionCategoryEnum.valueOf(category);
		final String type = groupArray[2];
		final ActionTypeEnum typeEnum = ActionItem.ActionTypeEnum.valueOf(type);
		arguments.add(new ArgumentDescriptor(groupArray[0], categoryEnum, typeEnum));
		return getActionItemRegex(categoryEnum, typeEnum);
	}

	private static String buildActionItemRegex_2args(
		final List<ArgumentDescriptor> arguments, 
		final String[] groupArray
	) {
		final String category = groupArray[0];
		final ActionCategoryEnum categoryEnum = ActionItem.ActionCategoryEnum.valueOf(category);
		final String type = groupArray[1];
		final ActionTypeEnum typeEnum = ActionItem.ActionTypeEnum.valueOf(type);
		arguments.add(new ArgumentDescriptor(categoryEnum, typeEnum));
		return getActionItemRegex(categoryEnum, typeEnum);
	}

	private static String buildActionItemRegex_1arg(
		final List<ArgumentDescriptor> arguments, 
		final String[] groupArray
	) {
		final String category = groupArray[0];
		final ActionCategoryEnum categoryEnum = ActionItem.ActionCategoryEnum.valueOf(category);
		arguments.add(new ArgumentDescriptor(categoryEnum, ActionItem.ActionTypeEnum.string));
		return getActionItemRegex(categoryEnum, ActionItem.ActionTypeEnum.string);
	}

	private static String getActionItemRegex(
		final ActionCategoryEnum categoryEnum,
		final ActionTypeEnum typeEnum
	) {
		final Optional<ActionItem> findFirst = ACTION_ITEMS.stream().filter(actionItem -> actionItem.category.equals(categoryEnum) && actionItem.kind.equals(typeEnum)).findFirst();
		return findFirst.isPresent() ? findFirst.get().regex : null;
	}
	
	private static boolean hadDeclaredAll(final String[] groupArray) {
		return groupArray.length == 3;
	}

	private static boolean hasDeclaredCategoryAndType(final String[] groupArray) {
		return groupArray.length == 2;
	}

	private static boolean hasOnlyDeclaredActionItemCategory(final String[] groupArray) {
		return groupArray.length == 1;
	}
	
	public static Object buildActionAdapterArgument(
		final IActionItemRepository repoSetup,
		String group
	) {
		group = group.replaceAll("\\*", "");
		if(repoSetup.getUserVariables().containsKey(group)) {
			Object object = repoSetup.getUserVariables().get(group);
			if(!(object instanceof String)) {
				return object;
			}
			if(isInputVariable(group)) {
				String value = (String) object;
				return handleValueWithNestedVars(repoSetup, value);
			}
		}
		return group;
	}

	private static String handleValueWithNestedVars(
		final IActionItemRepository repoSetup,
		String value
	) {
		final Pattern p = Pattern.compile(Property.ACTION_ITEM_VAR_REGEX, Pattern.MULTILINE);
		final Matcher m = p.matcher(value);
		int pos = 0;
		while(m.find()) {
			final String varName = m.group(pos + 1);
			if(repoSetup.getUserVariables().containsKey(varName)) {
				final Object varValue = repoSetup.getUserVariables().get(varName);
				value = value.replaceFirst("\\" + varName + "\\b", (String) varValue);
			}
		}
		return value;
	}

	private static boolean isInputVariable(
		final String group
	) {
		return 	group.startsWith("$")
				&& 
				!group.substring(1).contains("$")
				&& 
				!group.substring(1).contains(Property.DEFAULT_PARAM_SEPARATOR);
	}
}