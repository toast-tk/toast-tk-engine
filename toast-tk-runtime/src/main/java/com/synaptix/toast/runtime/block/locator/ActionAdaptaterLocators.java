package com.synaptix.toast.runtime.block.locator;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.TestLine;
import com.synaptix.toast.runtime.bean.TestLineDescriptor;

@Singleton
public class ActionAdaptaterLocators {

	@Inject
	private ActionAdaptaterLocator actionAdaptaterLocator;

	private final Map<String, ActionAdaptaterLocator> actionAdaptaterLocators;

	ActionAdaptaterLocators() {
		this.actionAdaptaterLocators = new HashMap<>();
	}


	public ActionAdaptaterLocator getActionCommandDescriptor(
			final TestBlock block,
			final TestLine line
	) throws NoActionAdapterFound {
		TestLineDescriptor testLineDescriptor = new TestLineDescriptor(block, line);
		actionAdaptaterLocator.setTestLineDescriptor(testLineDescriptor);
		actionAdaptaterLocator.findActionCommandDescriptor();
		return actionAdaptaterLocator;
	}

	//FIXME: review the hashcode mechanism
	/*public ActionAdaptaterLocator getActionCommandDescriptor(final ActionAdaptaterLocator actionAdaptaterLocator) {
		final String name = Integer.toString(actionAdaptaterLocator.hashCode());
		final ActionAdaptaterLocator foundActionAdapterLocator = actionAdaptaterLocators.get(name);

		if (foundActionAdapterLocator == null) {
			actionAdaptaterLocators.put(name, actionAdaptaterLocator);
			actionAdaptaterLocator.findActionCommandDescriptor();
			return actionAdaptaterLocator;
		}

		return foundActionAdapterLocator;
	}*/
}