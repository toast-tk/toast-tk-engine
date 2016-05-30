package com.synaptix.toast.runtime.result;

import com.synaptix.toast.dao.domain.api.test.ITestResult;

/**
 * Created by Nicolas on 26/05/2016.
 */
public interface IResultHandler<T> {
    public ITestResult result(T value, String expected);
}
