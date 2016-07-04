package io.toast.tk.adapter.swing.component;

import io.toast.tk.adapter.swing.AbstractSwingPage;

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