package com.synaptix.toast.core.guice;

import java.awt.event.AWTEventListener;

public interface FilteredAWTEventListener extends AWTEventListener {

	long getEventMask();
}