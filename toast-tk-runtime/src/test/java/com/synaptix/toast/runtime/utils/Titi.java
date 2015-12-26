package com.synaptix.toast.runtime.utils;

import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;


@ActionAdapter(value=ActionAdapterKind.service, name="titi-service")
public class Titi {
	@Action(action = "Titi", description = "")
	public void blabla() {
	}
}
