package io.toast.tk.runtime.utils;

import io.toast.tk.core.adapter.ActionAdapterKind;
import io.toast.tk.core.annotation.Action;
import io.toast.tk.core.annotation.ActionAdapter;


@ActionAdapter(value=ActionAdapterKind.service, name="titi-service")
public class Titi {
	@Action(action = "Titi", description = "")
	public void blabla() {
	}
}
