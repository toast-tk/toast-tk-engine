package com.synaptix.toast.core.agent.inspection;

import java.awt.Component;
import java.util.Set;

import com.synaptix.toast.core.agent.interpret.AWTCapturedEvent;

public interface ISwingInspectionServer {

	void highlight(
		String selectedValue);

	String getComponentLocator(
		Component component);

	void publishRecordEvent(
		AWTCapturedEvent eventObject);

	void publishInterpretedEvent(
		String sentence);

	Set<String> scan(
		boolean b);
}
