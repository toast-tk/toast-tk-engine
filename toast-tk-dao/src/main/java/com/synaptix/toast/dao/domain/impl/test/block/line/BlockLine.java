package com.synaptix.toast.dao.domain.impl.test.block.line;

import java.util.List;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.synaptix.toast.dao.domain.api.test.ITestResult;

import junit.framework.TestResult;

@Embedded
public class BlockLine {

	private List<String> cells;

	private ITestResult testResult;

	public BlockLine() {
	}

	public BlockLine(
		List<String> cells) {
		this.setCells(cells);
	}

	public List<String> getCells() {
		return cells;
	}

	public void setCells(
		List<String> cells) {
		this.cells = cells;
	}

	public ITestResult getTestResult() {
		return testResult;
	}

	public void setTestResult(
			ITestResult testResult) {
		this.testResult = testResult;
	}
}
