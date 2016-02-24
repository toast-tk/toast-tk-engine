package com.synaptix.toast.runtime.bean;

import com.synaptix.toast.runtime.bean.ActionItem.ActionCategoryEnum;
import com.synaptix.toast.runtime.bean.ActionItem.ActionTypeEnum;

public class ArgumentDescriptor {

	public ActionCategoryEnum categoryEnum;

	public ActionTypeEnum typeEnum;

	public String name;

	public String varName;

	public int index;
	
	public ArgumentDescriptor() {
		
	}
	
	public ArgumentDescriptor(
		final ActionCategoryEnum categoryEnum,
		final ActionTypeEnum typeEnum
	) {
		this.categoryEnum = categoryEnum;
		this.typeEnum = typeEnum;
	}
	
	public ArgumentDescriptor(
		final String name,
		final ActionCategoryEnum categoryEnum,
		final ActionTypeEnum typeEnum
	) {
		this.name = name;
		this.categoryEnum = categoryEnum;
		this.typeEnum = typeEnum;
	}
}