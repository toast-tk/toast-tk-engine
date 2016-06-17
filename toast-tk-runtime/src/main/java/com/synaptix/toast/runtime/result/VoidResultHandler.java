package com.synaptix.toast.runtime.result;

import com.synaptix.toast.core.report.SuccessResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.runtime.IActionItemRepository;

public class VoidResultHandler extends AbstractResultHandler<Void> {

    public VoidResultHandler(IActionItemRepository objectRepository) {
        super(objectRepository);
    }

    @Override
    public ITestResult result(Void value, String expected) {
        return new SuccessResult();
    }

}