package com.synaptix.toast.runtime.bean;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

import com.synaptix.toast.core.annotation.Action;

public class ActionCommandDescriptor {

	public Method method;

	public CommandArgumentDescriptor descriptor;

	public Matcher matcher;

	private boolean isMapped;

	private String actionMapping;

	public ActionCommandDescriptor(
		final Method method,
		final Matcher matcher,
		final CommandArgumentDescriptor descriptor
	) {
		this.method = method;
		this.matcher = matcher;
		this.descriptor = descriptor;
	}
	
	public String getActionId(){
		return method.getAnnotation(Action.class).id();
	}

	public void setIsMappedMethod(boolean isMapped) {
		this.isMapped = isMapped;
	}
	
	public boolean isMappedMethod() {
		return isMapped;
	}

	public void setActionMapping(String actionMapping) {
		this.actionMapping = actionMapping;
	}
	
	public String getActionMapping() {
		return this.actionMapping;
	}
}