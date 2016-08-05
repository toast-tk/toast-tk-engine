package io.toast.tk.adapter.swing.component;

import io.toast.tk.core.adapter.AutoSwingType;
import io.toast.tk.core.runtime.ISwingElementDescriptor;

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