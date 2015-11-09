package com.synaptix.toast.runtime.bean;

public class TestEntityProperty {
	public final String testName;

	public final String appName;

	public final String entityType;

	public TestEntityProperty(
		String testName,
		String appName,
		String entityType) {
		this.testName = testName;
		this.appName = appName;
		this.entityType = entityType;
	}
}
