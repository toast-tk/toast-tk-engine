package io.toast.tk.runtime.result;

import io.toast.tk.dao.core.report.SuccessResult;
import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.runtime.IActionItemRepository;

public class VoidResultHandler extends AbstractResultHandler<Void> {

    public VoidResultHandler(IActionItemRepository objectRepository) {
        super(objectRepository);
    }
    
    @Override
    public ITestResult buildResult(Void value, String expected) {
        return new SuccessResult();
    }

}