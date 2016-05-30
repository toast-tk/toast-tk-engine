package com.synaptix.toast.runtime.result;

import com.synaptix.toast.core.report.SuccessResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.runtime.IActionItemRepository;

public class IntegerResultHandler extends AbstractResultHandler<Integer> {

    public IntegerResultHandler(IActionItemRepository objectRepository) {
        super(objectRepository);
    }

    @Override
    public ITestResult result(Integer value, String expected) {
        ITestResult result = new SuccessResult(value.toString());

        return result;
    }

}