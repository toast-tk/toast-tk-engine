package com.synaptix.toast.adapter.cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.synaptix.toast.core.annotation.Action;

public final class ActionMethodCache {
	
	private final Map<Class<?>, List<Method>> actionMethods;

	private static final ActionMethodCache INSTANCE = new ActionMethodCache();
	
	public static ActionMethodCache getInstance() {
		return INSTANCE;
	}
	
	ActionMethodCache() {
		this.actionMethods = new HashMap<>();
	}
	
	public List<Method> getActionMethods(final Class<?> actionAdapterClass) {
		return actionMethods.computeIfAbsent(actionAdapterClass, actionClass -> searchActionMethods(actionAdapterClass));
	}
	
	private static List<Method> searchActionMethods(final Class<?> actionAdapterClass) {
		final Method[] methods = actionAdapterClass.getMethods();
		final List<Method> actionMethods = new ArrayList<>(methods.length);
		Arrays.stream(methods).forEach(method -> addActionMethod(actionMethods, method));
		return actionMethods;
	}

	private static void addActionMethod(
		final List<Method> actionMethods,
		final Method method
	) {
		final Action action = method.getAnnotation(Action.class);
		if (action != null) {
			actionMethods.add(method);
		}
	}
}