package io.toast.tk.runtime.result;

import io.toast.tk.dao.core.report.SuccessResult;
import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.runtime.IActionItemRepository;

public class BooleanResultHandler extends AbstractResultHandler<Boolean> {

    public BooleanResultHandler(IActionItemRepository objectRepository) {
        super(objectRepository);
    }
    
	@Override
	protected ITestResult buildResult(Boolean value, String expected) {
		ITestResult result = new SuccessResult(value.toString());
        return result;
	}

}