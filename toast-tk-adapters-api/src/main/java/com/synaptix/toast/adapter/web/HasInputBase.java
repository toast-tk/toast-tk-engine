package com.synaptix.toast.adapter.web;

import com.synaptix.toast.dao.domain.api.test.ITestResult;

public interface HasInputBase<T> {

	ITestResult setInput(final T e) throws Exception;
}
