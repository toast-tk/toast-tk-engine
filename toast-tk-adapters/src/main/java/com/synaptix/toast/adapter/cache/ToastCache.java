package com.synaptix.toast.adapter.cache;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import com.synaptix.toast.adapter.FixtureDescriptor;
import com.synaptix.toast.adapter.FixtureService;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;

public final class ToastCache {
	
	private static final Logger LOG = LogManager.getLogger(ToastCache.class);

	private static final ToastCache INSTANCE = new ToastCache();
	
	public static ToastCache getInstance() {
		return INSTANCE;
	}
	
	private final Set<Method> actionMethods;

	private final Map<Class<?>, List<Method>> actionMethodsByClass;
	
	private final Set<FixtureDescriptor> fixtureDescriptors; 
	
	private final Set<Class<?>> services;
	
	private final Set<FixtureService> fixtureServices;
	
	private final Map<Class<?>, FixtureService> fixtureServicesByClass;
	
	private final Map<Class<?>, FixtureDescriptor> fixtureDescriptorsByClass;
	
	private ToastCache() {
		this.actionMethods = new HashSet<>(2048);
		this.actionMethodsByClass = new HashMap<>(512);
		this.fixtureDescriptors = new HashSet<>(2048);
		this.fixtureServices = new HashSet<>(2048);
		this.services = new HashSet<>(512);
		this.fixtureServicesByClass = new HashMap<>(512);
		this.fixtureDescriptorsByClass = new NoExceptionMap(2048);
		initDatas();
	}
	
	private void initDatas() {
		actionMethods.addAll(buildMethodAnnotationsReflection().getMethodsAnnotatedWith(Action.class));
		actionMethods.stream().forEach(method -> addMethod(method));
		services.addAll(buildTypeAnnotationReflection().getTypesAnnotatedWith(ActionAdapter.class));
		services.stream().forEach(service -> buildAndAddFixtureService(service));
		fixtureServices.stream().forEach(fixtureService -> fixtureServicesByClass.putIfAbsent(fixtureService.clazz, fixtureService));
		actionMethods.forEach(actionMethod -> buildAndAddFixtureDescriptor(actionMethod));
		fixtureDescriptors.forEach(fixtureDescriptor -> fixtureDescriptorsByClass.putIfAbsent(getClass(fixtureDescriptor.name), fixtureDescriptor));
	}

	private static Class<?> getClass(final String className) {
		try {
			return Class.forName(className);
		}
		catch(final Exception e) {
			LOG.error(e.getMessage(), e);
			return Exception.class;
		}
	}
	
	private void addMethod(final Method method) {
		final Class<?> methodClass = method.getDeclaringClass();
		actionMethodsByClass.computeIfAbsent(methodClass, t -> actionMethodsByClass.put(t, new ArrayList<Method>()));
		actionMethodsByClass.get(methodClass).add(method);
	}

	private static Reflections buildMethodAnnotationsReflection() {
		return new Reflections(new MethodAnnotationsScanner());
	}

	private void buildAndAddFixtureDescriptor(final Method method) {
		final Action annotation = method.getAnnotation(Action.class);
		final Class<?> declaringClass = method.getDeclaringClass();
		final ActionAdapter docAnnotation = declaringClass.getAnnotation(ActionAdapter.class);
		final String fixtureKind = docAnnotation != null ? docAnnotation.value().name() : "undefined";
		fixtureDescriptors.add(new FixtureDescriptor(declaringClass.getSimpleName(), fixtureKind, annotation.action(), annotation.description()));
	}
	
	private void buildAndAddFixtureService(final Class<?> service) {
		if(!Modifier.isAbstract(service.getModifiers())) {
			final ActionAdapter docAnnotation = service.getAnnotation(ActionAdapter.class);
			fixtureServices.add(new FixtureService(service, docAnnotation.value(), docAnnotation.name()));
		}
	}
	
	private static Reflections buildTypeAnnotationReflection() {
		return new Reflections(new TypeAnnotationsScanner());
	}

	public Set<Method> getActionMethods() {
		return actionMethods;
	}

	public Map<Class<?>, List<Method>> getActionMethodsByClass() {
		return actionMethodsByClass;
	}

	public List<Method> getActionMethodsByClass(final Class<?> actionAdaptaterClass) {
		return actionMethodsByClass.get(actionAdaptaterClass);
	}
	
	public Set<FixtureDescriptor> getFixtureDescriptors() {
		return fixtureDescriptors;
	}

	public Set<Class<?>> getServices() {
		return services;
	}

	public Set<FixtureService> getFixtureServices() {
		return fixtureServices;
	}

	public Map<Class<?>, FixtureService> getFixtureServicesByClass() {
		return fixtureServicesByClass;
	}

	public Map<Class<?>, FixtureDescriptor> getFixtureDescriptorsByClass() {
		return fixtureDescriptorsByClass;
	}
}