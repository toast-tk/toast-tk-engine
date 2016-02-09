package com.synaptix.toast.dao.domain.impl.test.block.line;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.dao.domain.api.test.IRunnableTest;

@Entity(value = "test", noClassnameStored = true)
@Embedded
public class TestLine implements IRunnableTest {

	private String test;

	private String expected;

	@Embedded
	private TestResult testResult;

	/**
	 * Test comment
	 */
	private String comment;


	private long executionTime = 0;

	public TestLine() {
	}

	public TestLine(
			String test,
			String expected,
			String comment) {
		this.setTest(test);
		this.setExpected(expected);
		this.setComment(comment);
	}

	@Override
	public TestResult getTestResult() {
		return testResult;
	}

	@Override
	public void setTestResult(
			TestResult testResult) {
		this.testResult = testResult;
	}

	public String getTest() {
		return test;
	}

	public void setTest(
			String test) {
		this.test = test;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(
			String expected) {
		this.expected = expected;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(
			String comment) {
		this.comment = comment;
	}

	@Override
	public long getExecutionTime() {
		return executionTime;
	}

	public void setExcutionTime(long executionTime){
		this.executionTime = executionTime;
	}
}