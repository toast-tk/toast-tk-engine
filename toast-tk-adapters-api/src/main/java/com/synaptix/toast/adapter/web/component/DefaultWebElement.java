package com.synaptix.toast.adapter.web.component;

import com.synaptix.toast.core.adapter.AutoWebType;
import com.synaptix.toast.core.runtime.IWebElement;

public class DefaultWebElement implements IWebElement {

	public String locator;

	public LocationMethod method;

	public int position;

	private AutoWebType type;

	private String name;

	public DefaultWebElement(
		String name,
		AutoWebType type,
		String locator,
		LocationMethod method,
		int position) {
		this.locator = locator;
		this.method = method;
		this.position = position;
		this.type = type;
		this.name = name;
	}

	@Override
	public String getLocator() {
		return locator;
	}

	@Override
	public void setLocator(
		String locator) {
		this.locator = locator;
	}

	@Override
	public LocationMethod getMethod() {
		return method;
	}

	@Override
	public void setMethod(
		LocationMethod method) {
		this.method = method;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public void setPosition(
		int position) {
		this.position = position;
	}

	@Override
	public void setName(
		String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AutoWebType getType() {
		return type;
	}

	@Override
	public void setType(
		AutoWebType type) {
		this.type = type;
	}
}
