package io.toast.tk.runtime.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.toast.tk.core.annotation.Action;
import io.toast.tk.runtime.ActionItemDescriptionCollector;
import io.toast.tk.runtime.IActionItemRepository;
import io.toast.tk.runtime.action.item.ActionItemRegexHolder;
import io.toast.tk.runtime.bean.ActionItem;
import io.toast.tk.runtime.bean.ArgumentDescriptor;
import io.toast.tk.runtime.bean.CommandArgumentDescriptor;
import io.toast.tk.runtime.bean.ActionItem.ActionCategoryEnum;
import io.toast.tk.runtime.bean.ActionItem.ActionTypeEnum;
import io.toast.tk.runtime.constant.Property;

public class ArgumentHelper {
	
	private static final List<ActionItem> ACTION_ITEMS = ActionItemDescriptionCollector.initActionItems();

	private static final Pattern ACTION_ITEM_VAR_PATTERN_1 = Pattern.compile(Property.ACTION_ITEM_VAR_REGEX_1, Pattern.MULTILINE);

	private static final Pattern ACTION_ITEM_VAR_PATTERN_2 = Pattern.compile(Property.ACTION_ITEM_VAR_REGEX_2, Pattern.MULTILINE);

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

	private static String buildActionItemRegex_3args(
		final List<ArgumentDescriptor> arguments, 
		final String[] groupArray
	) {
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
			if(isInputVariable(object.toString())) {
				final String value = (String) object;
				return handleValueWithNestedVars(repoSetup, value);
			}
			return object;
		}
		return group;
	}

	private static String handleValueWithNestedVars(
		final IActionItemRepository repoSetup,
		String value
	) {
		// First we check Vars with parentesis 
		value = handleValueWithNestedVars(repoSetup, ACTION_ITEM_VAR_PATTERN_1, value);
		// Then without
		return handleValueWithNestedVars(repoSetup, ACTION_ITEM_VAR_PATTERN_2, value);
	}
	private static String handleValueWithNestedVars(
		final IActionItemRepository repoSetup, Pattern pattern,
		String value
	) {
		final Matcher m = pattern.matcher(value);
		int pos = 0;
		while(m.find()) {
			String varName = m.group(pos + 1);
			if(pattern.equals(ACTION_ITEM_VAR_PATTERN_1)) {
				varName = varName.substring(1, varName.length() - 1);
			}
			if(repoSetup.getUserVariables().containsKey(varName)) {
				String varValue = repoSetup.getUserVariables().get(varName).toString();
				if(!varName.equals(varValue)){
					String valueToReplace = "\\" + varName + "\\b";
					if(pattern.equals(ACTION_ITEM_VAR_PATTERN_1)) {
						valueToReplace = "\\(" + valueToReplace + "\\)";
					}
					value = value.replaceFirst(valueToReplace, 
							varValue.replace("\\", "\\\\").replace("$", "\\$"));	
				}
			}
		}
		return value;
	}

	private static boolean isInputVariable(
		final String group
	) {
		return 	group.contains("$")
				&& 
				!group.substring(1).contains(Property.DEFAULT_PARAM_SEPARATOR);
	}
}