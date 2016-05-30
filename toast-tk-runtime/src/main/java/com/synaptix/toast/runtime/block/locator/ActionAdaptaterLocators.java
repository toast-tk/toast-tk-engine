package com.synaptix.toast.runtime.block.locator;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Injector;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.TestLine;
import com.synaptix.toast.runtime.bean.TestLineDescriptor;

public class ActionAdaptaterLocators {

	private static final ActionAdaptaterLocators INSTANCE = new ActionAdaptaterLocators();
	
	public static ActionAdaptaterLocators getInstance() {
		return INSTANCE;
	}
	
	private final Map<String, ActionAdaptaterLocator> actionAdaptaterLocators;
	
	ActionAdaptaterLocators() {
		this.actionAdaptaterLocators = new HashMap<>();
	}
	
	public ActionAdaptaterLocator getActionCommandDescriptor(
			final TestBlock block, 
			final TestLine line,
			final Injector injector
	) {
		return getActionCommandDescriptor(new ActionAdaptaterLocator(new TestLineDescriptor(block, line), injector));
	}
	
	public ActionAdaptaterLocator getActionCommandDescriptor(final TestLineDescriptor testLineDescriptor, final Injector injector) {
		return getActionCommandDescriptor(new ActionAdaptaterLocator(testLineDescriptor, injector));
	}
	
	public ActionAdaptaterLocator getActionCommandDescriptor(final ActionAdaptaterLocator actionAdaptaterLocator) {
		final String name = Integer.toString(actionAdaptaterLocator.hashCode());
		final ActionAdaptaterLocator findedActionAdaptaterLocator = actionAdaptaterLocators.get(name);

		if(findedActionAdaptaterLocator == null) {
			actionAdaptaterLocators.put(name, actionAdaptaterLocator);
			actionAdaptaterLocator.findActionCommandDescriptor();
			return actionAdaptaterLocator;
		}

		return findedActionAdaptaterLocator;
	}
}