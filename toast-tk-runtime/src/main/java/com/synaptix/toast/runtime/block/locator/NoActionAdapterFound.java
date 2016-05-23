package com.synaptix.toast.runtime.block.locator;

public class NoActionAdapterFound extends Exception {

	public NoActionAdapterFound(String testLineAction) {
		super("No ActionAdapter found for: " + testLineAction);
	}

}
