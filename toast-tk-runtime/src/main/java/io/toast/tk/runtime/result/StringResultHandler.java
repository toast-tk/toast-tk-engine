package io.toast.tk.runtime.result;

import io.toast.tk.dao.core.report.SuccessResult;
import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.runtime.IActionItemRepository;

class StringResultHandler extends AbstractResultHandler<String> {

	StringResultHandler(IActionItemRepository objectRepository) {
		super(objectRepository);
	}

	@Override
	protected ITestResult buildResult(String value, String expected) {
		return new SuccessResult(value);
	}

}