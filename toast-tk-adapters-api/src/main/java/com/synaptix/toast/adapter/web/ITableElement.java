package com.synaptix.toast.adapter.web;

import java.util.List;

public interface ITableElement {

	public void dbClickAtRow(
		int index);

	public int getNbRows();

	public List<String> getColumns();

	public String getValue(
		String columnName,
		int row);

	public String getValue(
		int col,
		int row);

	public boolean containsText(
		String text);
}
