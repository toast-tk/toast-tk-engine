package com.synaptix.toast.core.net.request;

public class TableCommandRequestQueryCriteria {

	public final String lookupCol;

	public final String lookupValue;

	public TableCommandRequestQueryCriteria(
		String lookupCol,
		String lookupValue) {
		this.lookupCol = lookupCol;
		this.lookupValue = lookupValue;
	}

	/**
	 * for serialization purpose only
	 */
	protected TableCommandRequestQueryCriteria() {
		this(null, null);
	}
}
