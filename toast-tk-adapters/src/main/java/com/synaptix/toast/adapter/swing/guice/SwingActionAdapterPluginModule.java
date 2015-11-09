package com.synaptix.toast.adapter.swing.guice;

import com.synaptix.toast.adapter.swing.handler.DefaultSwingCustomWidgetHandler;
import com.synaptix.toast.core.guice.AbstractComponentAdapterModule;

public class SwingActionAdapterPluginModule extends AbstractComponentAdapterModule {

	@Override
	protected void configureModule() {
		addTypeHandler(DefaultSwingCustomWidgetHandler.class);
	}
}
