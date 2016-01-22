package com.synaptix.toast.core.net.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CommandRequest implements IIdRequest {

	public boolean isCustom;

	public String b64ScreenShot;

	public String customCommand;

	public String itemType;

	public String item;

	public COMMAND_TYPE action;

	public String value;

	protected String _id;

	public enum COMMAND_TYPE {
		SET("set"), CLICK("click"), EXISTS("exists"), CLEAR("clear"), SELECT("select"), GET("get"),
		DOUBLE_CLICK("doubleClick"), SELECT_MENU("selectMenu"), FIND("find"), COUNT("count");

		private final String type;

		COMMAND_TYPE(
			String type) {
			this.type = type;
		}
	}

	// for serialization purpose only
	protected CommandRequest() {
		this._id = null;
		this.isCustom = false;
		this.customCommand = null;
		this.itemType = null;
		this.item = null;
		this.action = null;
		this.value = null;
	}

	protected CommandRequest(
		CommandRequestBuilder builder) {
		this._id = builder.id;
		this.isCustom = builder.isCustom;
		this.customCommand = builder.customCommand;
		this.itemType = builder.itemType;
		this.item = builder.item;
		this.action = builder.action;
		this.value = builder.value;
	}

	public static class CommandRequestBuilder {

		protected final String id;

		protected boolean isCustom;

		protected String customCommand;

		protected String itemType;

		protected String item;

		protected COMMAND_TYPE action;

		protected String value;

		public CommandRequestBuilder(
			String id) {
			this.id = id;
		}

		public CommandRequestBuilder ofType(
			String itemType) {
			this.itemType = itemType;
			return this;
		}

		public CommandRequestBuilder with(
			String item) {
			this.item = item;
			return this;
		}

		public CommandRequestBuilder value(
			String value) {
			this.value = value;
			return this;
		}

		public CommandRequestBuilder atPos(
			String pos) {
			this.value = pos;
			return this;
		}

		public CommandRequestBuilder sendKeys(
			String value) {
			this.value = value;
			this.action = COMMAND_TYPE.SET;
			return this;
		}

		public CommandRequestBuilder click() {
			this.action = COMMAND_TYPE.CLICK;
			return this;
		}

		public CommandRequestBuilder exists() {
			this.action = COMMAND_TYPE.EXISTS;
			return this;
		}

		public CommandRequestBuilder getValue() {
			this.action = COMMAND_TYPE.GET;
			return this;
		}

		public CommandRequestBuilder select(
			String itemName) {
			this.action = COMMAND_TYPE.SELECT;
			this.value = itemName;
			return this;
		}

		public CommandRequestBuilder clear() {
			this.action = COMMAND_TYPE.CLEAR;
			return this;
		}

		public CommandRequestBuilder asCustomCommand(
			String command) {
			this.isCustom = true;
			this.customCommand = command;
			this.value = command;
			return this;
		}

		public CommandRequest build() {
			return new CommandRequest(this);
		}
	}

	@Override
	public String getId() {
		return _id;
	}

	public boolean isExists() {
		return this.action.equals(COMMAND_TYPE.EXISTS);
	}

	public boolean isCustom() {
		return this.isCustom;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public String getBase64ScreenShot() {
		return b64ScreenShot;
	}
}
