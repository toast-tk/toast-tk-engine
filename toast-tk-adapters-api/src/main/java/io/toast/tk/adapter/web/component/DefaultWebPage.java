package io.toast.tk.adapter.web.component;

public class DefaultWebPage extends AbstractWebPage {
	
	//utilite ?
	@Override
	public void addElement(
		final String name,
		final String type,
		final String method,
		final String locator,
		final Integer position
	) {
		super.initElement(name, type, method, locator, position);
	}
}