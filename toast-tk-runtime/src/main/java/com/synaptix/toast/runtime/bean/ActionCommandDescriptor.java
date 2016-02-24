package com.synaptix.toast.runtime.bean;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

import com.synaptix.toast.core.annotation.Action;

public class ActionCommandDescriptor {

	public final Method method;

	public final CommandArgumentDescriptor descriptor;

	public final Matcher matcher;

	private boolean isMapped;

	private String actionMapping;

	private final String actionId;
	
	public ActionCommandDescriptor(
		final Method method,
		final Matcher matcher,
		final CommandArgumentDescriptor descriptor
	) {
		this.method = method;
		this.matcher = matcher;
		this.descriptor = descriptor;
		this.actionId = method.getAnnotation(Action.class).id();
	}
	
	public String getActionId() {
		return actionId;
	}

	public void setIsMappedMethod(final boolean isMapped) {
		this.isMapped = isMapped;
	}
	
	public boolean isMappedMethod() {
		return isMapped;
	}

	public void setActionMapping(final String actionMapping) {
		this.actionMapping = actionMapping;
	}
	
	public String getActionMapping() {
		return this.actionMapping;
	}
}