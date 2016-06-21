package com.synaptix.toast.runtime.block.locator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.TestLine;
import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.runtime.bean.TestLineDescriptor;

@Singleton
public class ActionAdaptaterLocators {

	@Inject
	private ActionAdaptaterLocator actionAdaptaterLocator;

	public ActionAdaptaterLocator getActionCommandDescriptor(
			final TestBlock block,
			final TestLine line
	) throws NoActionAdapterFound {
		TestLineDescriptor testLineDescriptor = new TestLineDescriptor(block, line);
		actionAdaptaterLocator.setTestLineDescriptor(testLineDescriptor);
		ActionCommandDescriptor descriptor = actionAdaptaterLocator.findActionCommandDescriptor();
		if(descriptor == null){
			throw new NoActionAdapterFound(line.getTest());
		}
		return actionAdaptaterLocator;
	}

}