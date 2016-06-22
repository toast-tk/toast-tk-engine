package com.synaptix.toast.runtime.result;

import com.synaptix.toast.dao.domain.api.test.ITestResult;


public interface IResultHandler<T> {
    public ITestResult result(T value, String expected);
}
