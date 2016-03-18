package com.synaptix.toast.runtime.block.locator;

public class ActionIndex {

	String item;

	int start;

	int end;
	
	public ActionIndex(
		final String item,
		final int start,
		final int end
	) {
		this.item = item;
		this.start = start;
		this.end = end;
	}
}