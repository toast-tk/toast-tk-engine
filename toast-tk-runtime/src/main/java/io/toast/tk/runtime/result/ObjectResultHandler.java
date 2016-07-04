package io.toast.tk.runtime.result;

import io.toast.tk.dao.core.report.SuccessResult;
import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.runtime.IActionItemRepository;

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