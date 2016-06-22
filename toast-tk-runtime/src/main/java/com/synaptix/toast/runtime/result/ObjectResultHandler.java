package com.synaptix.toast.runtime.result;

import com.synaptix.toast.core.report.SuccessResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.runtime.IActionItemRepository;

public class ObjectResultHandler extends AbstractResultHandler<Object> {

    public ObjectResultHandler(IActionItemRepository objectRepository) {
        super(objectRepository);
    }
    
	@Override
	protected ITestResult buildResult(Object value, String expected) {
		ITestResult result = new SuccessResult(value.toString());
        return result;
	}

}