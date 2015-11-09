package com.synaptix.toast.adapter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;

public class ActionAdapterCollector {

	/**
	 * List sentences defined in the framework
	 * 
	 * @return
	 */
	public static List<FixtureDescriptor> listAvailableSentences() {
		final List<FixtureDescriptor> out = new ArrayList<FixtureDescriptor>();
		final Reflections ref = new Reflections(new MethodAnnotationsScanner());
		final Set<Method> methodsAnnotatedWith = ref.getMethodsAnnotatedWith(Action.class);
		for(Method method : methodsAnnotatedWith) {
			Action annotation = method.getAnnotation(Action.class);
			Class<?> declaringClass = method.getDeclaringClass();
			ActionAdapter docAnnotation = declaringClass.getAnnotation(ActionAdapter.class);
			final String fixtureKind;
			if(docAnnotation != null) {
				fixtureKind = docAnnotation.value().name();
			}
			else {
				fixtureKind = "undefined";
			}
			out.add(new FixtureDescriptor(declaringClass.getSimpleName(), fixtureKind, annotation.action(), annotation.description()));
		}
		return out;
	}

	public static List<FixtureService> listAvailableServicesByReflection() {
		final List<FixtureService> out = new ArrayList<FixtureService>();
		final Reflections ref = new Reflections(new TypeAnnotationsScanner());
		final Set<Class<?>> services = ref.getTypesAnnotatedWith(ActionAdapter.class);
		for(Class<?> service : services) {
			if(!Modifier.isAbstract(service.getModifiers())) {
				ActionAdapter docAnnotation = service.getAnnotation(ActionAdapter.class);
				out.add(new FixtureService(service, docAnnotation.value(), docAnnotation.name()));
			}
		}
		return out;
	}

	public static List<FixtureService> listAvailableServicesByInjection(
		Injector injector) {
		final List<FixtureService> out = new ArrayList<FixtureService>();
		Map<Key<?>, Binding<?>> allBindings = injector.getAllBindings();
		for(Map.Entry<Key<?>, Binding<?>> bindingEntrySet : allBindings.entrySet()) {
			final Type type = bindingEntrySet.getKey().getTypeLiteral().getType();
			if(type instanceof Class) {
				final Class<?> beanClass = (Class<?>) type;
				if(beanClass.isAnnotationPresent(ActionAdapter.class)) {
					ActionAdapter docAnnotation = beanClass.getAnnotation(ActionAdapter.class);
					out.add(new FixtureService(beanClass, docAnnotation.value(), docAnnotation.name()));
				}
			}
		}
		return out;
	}
}
