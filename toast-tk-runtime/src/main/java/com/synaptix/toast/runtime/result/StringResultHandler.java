package com.synaptix.toast.runtime.result;

import com.synaptix.toast.core.report.SuccessResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.runtime.IActionItemRepository;

public class StringResultHandler extends AbstractResultHandler<String> {

    public StringResultHandler(IActionItemRepository objectRepository) {
        super(objectRepository);
    }

    @Override
    public ITestResult result(String value, String expected) {
        ITestResult result = new SuccessResult(value);

        if (!objectRepository.getUserVariables().containsKey(expected)) {
            objectRepository.getUserVariables().put(expected, value);
        }

        return result;
    }

}