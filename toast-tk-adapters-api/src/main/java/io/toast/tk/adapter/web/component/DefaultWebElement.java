package io.toast.tk.adapter.web.component;

import io.toast.tk.core.adapter.AutoWebType;
import io.toast.tk.core.runtime.IWebElementDescriptor;

public class DefaultWebElement implements IWebElementDescriptor {

	public String locator;

	public LocationMethod method;

	public int position;

	private AutoWebType type;

	private String name;

	private String referenceName;

	public DefaultWebElement(
		final String name,
		final AutoWebType type,
		final String locator,
		final LocationMethod method,
		final int position, 
		final String referenceName
	) {
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
	public void setLocator(final String locator) {
		this.locator = locator;
	}

	@Override
	public LocationMethod getMethod() {
		return method;
	}

	@Override
	public void setMethod(final LocationMethod method) {
		this.method = method;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public void setPosition(final int position) {
		this.position = position;
	}

	@Override
	public void setName(final String name) {
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
	public void setType(final AutoWebType type) {
		this.type = type;
	}

	@Override
	public String getReferenceName() {
		return referenceName;
	}

	@Override
	public void setReferenceName(final String referenceName) {
		this.referenceName = referenceName;
	}
}