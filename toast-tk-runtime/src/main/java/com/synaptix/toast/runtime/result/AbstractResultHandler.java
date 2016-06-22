package com.synaptix.toast.runtime.result;

import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult.ResultKind;
import com.synaptix.toast.runtime.IActionItemRepository;

public abstract class AbstractResultHandler<T> implements IResultHandler<T> {

    protected IActionItemRepository objectRepository;

    public AbstractResultHandler(IActionItemRepository objectRepository) {
        this.objectRepository = objectRepository;
    }
    
	@Override
    public ITestResult result(T value, String expected) {
		if (shouldStoreResult(expected)) {
			objectRepository.getUserVariables().put(expected, value);
		}
		ITestResult result = buildResult(value, expected);
		if(value == null){
			if(expected != null){
				markResultAsFailure(result);
			}
		}
		else if(expected != null && !value.toString().equals(expected)){
			markResultAsFailure(result);
		}
		/*else{
			result.setResultKind(ResultKind.SUCCESS);
		}*/
		return result;
    }

	private boolean shouldStoreResult(String expected) {
		return expected != null && 
			isVar(expected) 
			&& !objectRepository.getUserVariables().containsKey(expected);
	}

	private void markResultAsFailure(ITestResult result) {
		result.setIsSuccess(false);
		result.setIsFailure(true);
		result.setResultKind(ResultKind.FAILURE);
	}
	
	private boolean isVar(String expected){
		return expected.startsWith("$");
	}
	
	protected abstract ITestResult buildResult(T value, String expected);
}
