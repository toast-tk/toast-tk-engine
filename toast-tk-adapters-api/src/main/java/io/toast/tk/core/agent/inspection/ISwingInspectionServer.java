package io.toast.tk.core.agent.inspection;

import java.awt.Component;
import java.util.Set;

import io.toast.tk.core.agent.interpret.AWTCapturedEvent;

public interface ISwingInspectionServer {

	void highlight(final String selectedValue);

	String getComponentLocator(final Component component);

	void publishRecordEvent(final AWTCapturedEvent eventObject);

	void publishInterpretedEvent(final String sentence);

	Set<String> scan(final boolean b);
}