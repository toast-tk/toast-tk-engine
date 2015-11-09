package com.synaptix.toast.core.net.request;

import java.util.ArrayList;
import java.util.List;

import com.synaptix.toast.core.adapter.AutoSwingType;

public class TableCommandRequest extends CommandRequest {

	public final TableCommandRequestQuery query;

	// for serialization purpose only
	protected TableCommandRequest() {
		super();
		this.query = null;
	}

	private TableCommandRequest(TableCommandRequestBuilder builder) {
		super(builder);
		this.query = builder.query;
	}

	public static class TableCommandRequestBuilder extends
			CommandRequestBuilder {

		private TableCommandRequestQuery query;

		public TableCommandRequestBuilder(String id) {
			super(id);
			this.itemType = AutoSwingType.table.name();
		}

		public TableCommandRequestBuilder count() {
			this.action = COMMAND_TYPE.COUNT;
			return this;
		}

		public TableCommandRequestBuilder with(String item) {
			this.item = item;
			return this;
		}

		public TableCommandRequestBuilder find(String lookUpColumn,
				String lookUpValue, String outputColumn) {
			this.action = COMMAND_TYPE.FIND;
			this.value = lookUpValue;
			List<TableCommandRequestQueryCriteria> criteria = new ArrayList<TableCommandRequestQueryCriteria>();
			criteria.add(new TableCommandRequestQueryCriteria(lookUpColumn,
					lookUpValue));
			this.query = new TableCommandRequestQuery(criteria, outputColumn);
			return this;
		}

		public TableCommandRequestBuilder find(
				List<TableCommandRequestQueryCriteria> criteria) {
			this.action = COMMAND_TYPE.FIND;
			this.query = new TableCommandRequestQuery(criteria);
			return this;
		}

		public TableCommandRequestBuilder find(
				List<TableCommandRequestQueryCriteria> criteria,
				String outputColumn) {
			this.action = COMMAND_TYPE.FIND;
			this.query = new TableCommandRequestQuery(criteria, outputColumn);
			return this;
		}

		public TableCommandRequestBuilder doubleClick(String column,
				String value) {
			this.action = COMMAND_TYPE.DOUBLE_CLICK;
			this.value = value;
			List<TableCommandRequestQueryCriteria> criteria = new ArrayList<TableCommandRequestQueryCriteria>();
			criteria.add(new TableCommandRequestQueryCriteria(column, value));
			this.query = new TableCommandRequestQuery(criteria);
			return this;
		}

		public TableCommandRequestBuilder selectMenu(String menu,
				List<TableCommandRequestQueryCriteria> criteria) {
			this.action = COMMAND_TYPE.SELECT_MENU;
			this.value = menu;
			this.query = new TableCommandRequestQuery(criteria);
			return this;
		}

		public TableCommandRequestBuilder selectMenu(String menu,
				String column, String value) {
			this.action = COMMAND_TYPE.SELECT_MENU;
			this.value = menu;
			List<TableCommandRequestQueryCriteria> criteria = new ArrayList<TableCommandRequestQueryCriteria>();
			criteria.add(new TableCommandRequestQueryCriteria(column, value));
			this.query = new TableCommandRequestQuery(criteria);
			return this;
		}

		@Override
		public TableCommandRequest build() {
			return new TableCommandRequest(this);
		}
	}
}
