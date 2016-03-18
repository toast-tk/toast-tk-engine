package com.synaptix.toast.runtime.utils;

import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.annotation.ActionAdapter;

@ActionAdapter(value = ActionAdapterKind.service, name = "toto-service")
public class Toto extends Tata {

}
