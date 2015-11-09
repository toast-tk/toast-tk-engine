package com.synaptix.toast.adapter.swing.component;

import com.synaptix.toast.adapter.swing.SwingAutoElement;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.runtime.ISwingElement;

/**
 * input element
 * 
 * @author skokaina
 * 
 */
public class SwingSynTimeLineElement extends SwingAutoElement {

	public SwingSynTimeLineElement(
		ISwingElement element,
		IRemoteSwingAgentDriver driver) {
		super(element, driver);
	}

	public SwingSynTimeLineElement(
		ISwingElement element) {
		super(element);
	}
}