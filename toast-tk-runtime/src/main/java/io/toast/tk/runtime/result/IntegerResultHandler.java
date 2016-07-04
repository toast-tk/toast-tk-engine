package io.toast.tk.runtime.result;

import io.toast.tk.dao.core.report.SuccessResult;
import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.runtime.IActionItemRepository;

public class IntegerResultHandler extends AbstractResultHandler<Integer> {

    public IntegerResultHandler(IActionItemRepository objectRepository) {
        super(objectRepository);
    }
    
	@Override
	protected ITestResult buildResult(Integer value, String expected) {
		ITestResult result = new SuccessResult(value.toString());
	    return result;
	}

}