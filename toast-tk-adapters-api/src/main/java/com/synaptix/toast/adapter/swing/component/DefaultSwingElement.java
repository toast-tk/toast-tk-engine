package com.synaptix.toast.adapter.swing.component;

import com.synaptix.toast.core.adapter.AutoSwingType;
import com.synaptix.toast.core.runtime.ISwingElementDescriptor;

/**
 * 
 * @author skokaina
 * 
 */
public class DefaultSwingElement implements ISwingElementDescriptor {

	public String locator;

	private AutoSwingType type;

	private String name;

	public DefaultSwingElement(
		String name,
		AutoSwingType type,
		String locator) {
		this.locator = locator;
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
	public void setName(
		String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AutoSwingType getType() {
		return type;
	}

	@Override
	public void setType(
		AutoSwingType type) {
		this.type = type;
	}
}
