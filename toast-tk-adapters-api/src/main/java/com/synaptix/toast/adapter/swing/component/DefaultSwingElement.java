package com.synaptix.toast.adapter.swing.component;

import com.synaptix.toast.core.adapter.AutoSwingType;
import com.synaptix.toast.core.runtime.ISwingElementDescriptor;

public class DefaultSwingElement implements ISwingElementDescriptor {

	public final String locator;

	private final AutoSwingType type;

	private final String name;

	public DefaultSwingElement(
		final String name,
		final AutoSwingType type,
		final String locator
	) {
		this.locator = locator;
		this.type = type;
		this.name = name;
	}

	@Override
	public String getLocator() {
		return locator;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AutoSwingType getType() {
		return type;
	}
}