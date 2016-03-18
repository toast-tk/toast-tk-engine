package com.synaptix.toast.adapter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.synaptix.toast.adapter.cache.ToastCache;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;

public final class ActionAdapterCollector {

	private ActionAdapterCollector() {

	}
	
	public static List<FixtureDescriptor> listAvailableSentences() {
		final Set<Method> methodsAnnotatedWith = ToastCache.buildActionMethods();
		final List<FixtureDescriptor> out = new ArrayList<>(methodsAnnotatedWith.size());
		methodsAnnotatedWith.stream().forEach(method -> buildAndAddFixtureDescriptor(out, method));
		return out;
	}

	private static void buildAndAddFixtureDescriptor(
		final List<FixtureDescriptor> out, 
		final Method method
	) {
		final Action annotation = method.getAnnotation(Action.class);
		final Class<?> declaringClass = method.getDeclaringClass();
		final ActionAdapter docAnnotation = declaringClass.getAnnotation(ActionAdapter.class);
		final String fixtureKind = docAnnotation != null ? docAnnotation.value().name() : "undefined";
		out.add(new FixtureDescriptor(declaringClass.getSimpleName(), fixtureKind, annotation.action(), annotation.description()));
	}
	
	public static List<FixtureService> listAvailableServicesByReflection() {
		return new ArrayList<>(ToastCache.getInstance().getFixtureServices());
	}

	public static List<FixtureService> listAvailableServicesByInjection(final Injector injector) {
		final Set<Entry<Key<?>, Binding<?>>> bindings = injector.getAllBindings().entrySet();
		final List<FixtureService> out = new ArrayList<>(bindings.size());
		bindings.stream().forEach(bindingEntrySet -> buildAndAddFixtureService(out, bindingEntrySet));
		return out;
	}

	private static void buildAndAddFixtureService(
		final List<FixtureService> out,
		final Map.Entry<Key<?>, Binding<?>> bindingEntrySet
	) {
		final Type type = bindingEntrySet.getKey().getTypeLiteral().getType();
		if(type instanceof Class) {
			final Class<?> beanClass = (Class<?>) type;
			if(beanClass.isAnnotationPresent(ActionAdapter.class)) {
				final ActionAdapter docAnnotation = beanClass.getAnnotation(ActionAdapter.class);
				out.add(new FixtureService(beanClass, docAnnotation.value(), docAnnotation.name()));
			}
		}
	}
}