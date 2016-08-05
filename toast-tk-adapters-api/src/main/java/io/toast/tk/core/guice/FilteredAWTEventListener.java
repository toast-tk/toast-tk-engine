package io.toast.tk.core.guice;

import java.awt.event.AWTEventListener;

public interface FilteredAWTEventListener extends AWTEventListener {

	long getEventMask();
}