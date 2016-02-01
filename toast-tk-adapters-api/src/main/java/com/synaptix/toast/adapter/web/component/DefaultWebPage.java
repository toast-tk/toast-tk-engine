package com.synaptix.toast.adapter.web.component;

import java.util.List;

import com.synaptix.toast.adapter.web.AbstractWebPage;

public class DefaultWebPage extends AbstractWebPage {

	public void addElement(
		String name,
		String type,
		String method,
		String locator,
		Integer position) {
		super.initElement(name, type, method, locator, position);
	}
	
	@Override
	public List getAllWebElements() {
		return null;
	}
}
