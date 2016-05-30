package com.synaptix.toast.runtime.result;

import com.synaptix.toast.core.report.FailureResult;
import com.synaptix.toast.core.report.SuccessResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.runtime.IActionItemRepository;

class StringResultHandler extends AbstractResultHandler<String> {

	StringResultHandler(IActionItemRepository objectRepository) {
		super(objectRepository);
	}

	@Override
	public ITestResult result(String value, String expected) {
		if (!objectRepository.getUserVariables().containsKey(expected)) {
			objectRepository.getUserVariables().put(expected, value);
		}

		if (expected == null || expected.equals(value)) {
			return new SuccessResult(value);
		} else {
			return new FailureResult("Got " + value + " when " + expected + " was expected");
		}
	}

}