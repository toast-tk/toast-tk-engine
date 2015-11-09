package com.synaptix.toast.core.net.request;

import java.util.List;

public class TableCommandRequestQuery {

	public final String resultCol;

	public final List<TableCommandRequestQueryCriteria> criteria;

	/**
	 * for serialization purpose only
	 */
	public TableCommandRequestQuery() {
		this(null, null);
	}

	public TableCommandRequestQuery(
		List<TableCommandRequestQueryCriteria> criteria) {
		this(criteria, null);
	}

	public TableCommandRequestQuery(
		List<TableCommandRequestQueryCriteria> criteria,
		String resultCol) {
		this.criteria = criteria;
		this.resultCol = resultCol;
	}
}
