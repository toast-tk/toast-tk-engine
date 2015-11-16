package com.synaptix.toast.runtime.bean;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

public class ActionCommandDescriptor {
	public Method method;
	public CommandArgumentDescriptor descriptor;
	public Matcher matcher;

	public ActionCommandDescriptor(
		Method method,
		Matcher matcher,
		CommandArgumentDescriptor descriptor) {
		this.method = method;
		this.matcher = matcher;
		this.descriptor = descriptor;
	}
}
