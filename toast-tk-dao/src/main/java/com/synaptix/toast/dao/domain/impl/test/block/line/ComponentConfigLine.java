package com.synaptix.toast.dao.domain.impl.test.block.line;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.synaptix.toast.core.report.TestResult;

@Entity(value = "test", noClassnameStored = true)
@Embedded
public class ComponentConfigLine {

	private String testName;

	private String systemName;

	private String componentAssociation;

	private TestResult result;

	public TestResult getTestResult() {
		return result;
	}

	public ComponentConfigLine() {
	}

	public ComponentConfigLine(
		final String testName,
		final String systemName,
		final String componentAssociation
	) {
		this.testName = testName;
		this.systemName = systemName;
		this.componentAssociation = componentAssociation;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(final String testName) {
		this.testName = testName;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(final String systemName) {
		this.systemName = systemName;
	}

	public String getComponentAssociation() {
		return componentAssociation;
	}

	public void setComponentAssociation(final String componentAssociation) {
		this.componentAssociation = componentAssociation;
	}

	public void setResult(final TestResult result) {
		this.result = result;
	}
}