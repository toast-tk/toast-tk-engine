package io.toast.tk.runtime.result;

import io.toast.tk.dao.domain.api.test.ITestResult;


public interface IResultHandler<T> {
    public ITestResult result(T value, String expected);
}
