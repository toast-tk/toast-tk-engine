package io.toast.tk.runtime.utils;

import io.toast.tk.core.adapter.ActionAdapterKind;
import io.toast.tk.core.annotation.ActionAdapter;

@ActionAdapter(value = ActionAdapterKind.service, name = "toto-service")
public class Toto extends Tata {

}
