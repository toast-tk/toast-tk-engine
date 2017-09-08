package io.toast.tk.runtime.result;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import io.toast.tk.dao.core.report.SuccessResult;
import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.runtime.IActionItemRepository;

public class ObjectResultHandler extends AbstractResultHandler<Object> {

    public ObjectResultHandler(IActionItemRepository objectRepository) {
        super(objectRepository);
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
    private String resultBuetifier(Object value) {	
		try {
			return ((List)value).stream()
					.map(Object::toString)
					.collect(Collectors.joining(System.lineSeparator()))
					.toString();
		}
		catch(Exception e) {
			try {
		    	StringBuilder res = new StringBuilder();
				for(Entry entry : ((Map<?, ?>) value).entrySet()) {
					res.append(entry.getKey().toString() + " = " 
							+ entry.getValue().toString() 
							+ System.lineSeparator());
				}
				return res.toString();
			} catch(Exception e2) {
				if(value != null) {
					return value.toString();
				} else {
					return "";
				}
			}
		}
    }
        
	@Override
	protected ITestResult buildResult(Object value, String expected) {
		String res = resultBuetifier(value);
		
		ITestResult result = new SuccessResult(res);
        return result;
	}

}