package io.toast.tk.adapter.swing;

import java.awt.AWTEvent;

import io.toast.tk.core.guice.FilteredAWTEventListener;

public interface IAWTPriorityListener extends FilteredAWTEventListener{
	
	public void eventDispatched(AWTEvent event, AWTEventPriorityWrapper wrapper);

}
