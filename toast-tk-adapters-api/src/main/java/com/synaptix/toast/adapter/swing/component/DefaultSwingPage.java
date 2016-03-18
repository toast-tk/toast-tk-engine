package com.synaptix.toast.adapter.swing.component;

import com.synaptix.toast.adapter.swing.AbstractSwingPage;

public class DefaultSwingPage extends AbstractSwingPage {

	@Override
	public void addElement(
		final String name,
		final String type,
		final String locator
	) {
		super.initElement(name, type, locator);
	}
}