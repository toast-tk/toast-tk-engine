package com.synaptix.toast.runtime.bean;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

public class ActionCommandDescriptor {
	public Method method;

	public Matcher matcher;

	public ActionCommandDescriptor(
		Method method,
		Matcher matcher) {
		this.method = method;
		this.matcher = matcher;
	}
}
