package com.synaptix.toast.adapter.web;

import com.synaptix.toast.core.report.TestResult;

public interface HasInputBase<T> {

	public TestResult setInput(
		T e)
		throws Exception;
}
