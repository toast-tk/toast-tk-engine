package io.toast.tk.runtime.block.locator;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.toast.tk.dao.domain.impl.test.block.TestBlock;
import io.toast.tk.dao.domain.impl.test.block.line.TestLine;
import io.toast.tk.runtime.bean.ActionCommandDescriptor;
import io.toast.tk.runtime.bean.TestLineDescriptor;

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