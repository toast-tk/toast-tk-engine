package com.synaptix.toast.adapter.web.component;

import com.synaptix.toast.core.adapter.AutoWebType;
import com.synaptix.toast.core.runtime.IWebElementDescriptor;

public class DefaultWebElement implements IWebElementDescriptor {

	public String locator;

	public LocationMethod method;

	public int position;

	private AutoWebType type;

	private String name;

	private String referenceName;
	
	
	public DefaultWebElement(
		String name,
		AutoWebType type,
		String locator,
		LocationMethod method,
		int position, String referenceName) {
		this.locator = locator;
		this.method = method;
		this.position = position;
		this.type = type;
		this.name = name;
		this.setReferenceName(referenceName);
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

	@Override
	public String getReferenceName() {
		return referenceName;
	}

	@Override
	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}
}
