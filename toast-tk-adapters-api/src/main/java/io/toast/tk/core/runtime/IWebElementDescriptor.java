package io.toast.tk.core.runtime;

import io.toast.tk.core.adapter.AutoWebType;

public interface IWebElementDescriptor {

	/**
	 * locator methods to use in repo
	 * 
	 */
	public static enum LocationMethod {
		CSS,
		ID,
		XPATH
	}

	void setName(final String name);

	String getName();

	String getLocator();

	void setLocator(final String locator);

	LocationMethod getMethod();

	void setMethod(final LocationMethod method);

	int getPosition();

	void setPosition(final int position);

	AutoWebType getType();

	void setType(final AutoWebType type);

	String getReferenceName();

	void setReferenceName(final String referenceName);
}