package com.synaptix.toast.runtime.bean;


public class ActionItem {

	public String id;

	public String name;

	public ActionTypeEnum kind;

	public ActionCategoryEnum category;

	public String regex;

	public String replacement;

	protected ActionItem() {
	}

	public enum ActionCategoryEnum {
		value, variable, component
	}

	public enum ActionTypeEnum {
		string, swing, service, web, entity
	}
}
