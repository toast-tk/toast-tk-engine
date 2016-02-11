package com.synaptix.toast.dao.domain.impl.test.block.line;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.synaptix.toast.core.report.TestResult;

@Entity(value = "test", noClassnameStored = true)
@Embedded
public class WebPageConfigLine{

	private String elementName;

	private String type;

	private String method;

	private String locator;

	private int position;

	private TestResult result;

	public TestResult getTestResult() {
		return result;
	}

	public WebPageConfigLine(
		final String name,
		final String type,
		final String method,
		final String locator,
		final Integer position
	) {
		this.elementName = name;
		this.type = type;
		this.method = method;
		this.locator = locator;
		this.position = position;
	}

	public WebPageConfigLine() {

	}

	public void setResult(final TestResult result) {
		this.result = result;
	}

	public String getElementName() {
		return elementName;
	}

	public String getType() {
		return type;
	}

	public String getMethod() {
		return method;
	}

	public String getLocator() {
		return locator;
	}

	public Integer getPosition() {
		return position;
	}

	public TestResult getResult() {
		return result;
	}

	public void setElementName(final String elementName) {
		this.elementName = elementName;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public void setMethod(final String method) {
		this.method = method;
	}

	public void setLocator(final String locator) {
		this.locator = locator;
	}

	public void setPosition(final Integer position) {
		this.position = position;
	}
}