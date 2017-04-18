package io.toast.tk.runtime.result;

import java.util.List;
import java.util.stream.Collectors;

import io.toast.tk.dao.core.report.SuccessResult;
import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.runtime.IActionItemRepository;

public class ObjectResultHandler extends AbstractResultHandler<Object> {

    public ObjectResultHandler(IActionItemRepository objectRepository) {
        super(objectRepository);
    }
        
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected ITestResult buildResult(Object value, String expected) {
		String res = "";
		
		try {
			res = (String) ((List)value).stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
		}
		catch(Exception e) {
			res = value.toString();
		}
		
		ITestResult result = new SuccessResult(res);
        return result;
	}

}