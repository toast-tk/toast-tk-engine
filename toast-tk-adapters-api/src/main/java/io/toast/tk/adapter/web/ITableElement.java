package io.toast.tk.adapter.web;

import java.util.List;

public interface ITableElement {

	void dbClickAtRow(final int index);

	int getNbRows();

	List<String> getColumns();

	String getValue(final String columnName, final int row);

	String getValue(final int col, final int row);

	boolean containsText(final String text);
}