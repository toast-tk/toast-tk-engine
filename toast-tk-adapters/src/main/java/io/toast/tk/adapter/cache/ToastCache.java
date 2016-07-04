package io.toast.tk.adapter.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import io.toast.tk.adapter.FixtureDescriptor;
import io.toast.tk.adapter.FixtureService;
import io.toast.tk.core.annotation.Action;
import io.toast.tk.core.annotation.ActionAdapter;

public final class ToastCache {

	private static ToastCache INSTANCE;

	public static synchronized ToastCache getInstance() {
		if(INSTANCE == null){
			INSTANCE = new ToastCache();
		}
		return INSTANCE;
	}

	private final Set<Method> actionMethods;

	private final Map<Class<?>, List<Method>> actionMethodsByClass;

	private final Set<Class<?>> services;

	private final Set<FixtureService> fixtureServices;

	private final Map<Class<?>, FixtureService> fixtureServicesByClass;

	private final Map<Class<?>, FixtureDescriptor> fixtureDescriptorsByClass;

	private ToastCache() {
		this.actionMethods = new HashSet<>(2048);
		this.actionMethodsByClass = new HashMap<>(512);
		this.fixtureServices = new HashSet<>(2048);
		this.services = new HashSet<>(512);
		this.fixtureServicesByClass = new HashMap<>(512);
		this.fixtureDescriptorsByClass = new NoExceptionMap(2048);
		initDatas();
	}

	private void initDatas() {
		actionMethods.addAll(buildActionMethods());
		actionMethods.stream().forEach(method -> addMethod(method));
		services.addAll(buildTypeAnnotationReflection().getTypesAnnotatedWith(ActionAdapter.class));
		services.stream().forEach(service -> buildAndAddFixtureService(service));
		fixtureServices.stream().forEach(fixtureService -> fixtureServicesByClass.putIfAbsent(fixtureService.clazz, fixtureService));
	}

	public static Set<Method> buildActionMethods() {
		return buildMethodAnnotationsReflection().getMethodsAnnotatedWith(Action.class);
	}

	private void addMethod(final Method method) {
		final Class<?> methodClass = method.getDeclaringClass();
		actionMethodsByClass.computeIfAbsent(methodClass, t -> actionMethodsByClass.put(t, new ArrayList<Method>()));
		actionMethodsByClass.get(methodClass).add(method);
	}

	private static Reflections buildMethodAnnotationsReflection() {
		return new Reflections( new MethodAnnotationsScanner());
	}

	public void addActionAdapter(final Class<?> service){
		final ActionAdapter docAnnotation = service.getAnnotation(ActionAdapter.class);
		if(docAnnotation != null){
			List<Method> methods = getMethodsAnnotatedWith(service, Action.class);
			if(methods != null){
				for(Method m : methods){
					addMethod(m);
				}
			}
		}
	}

	public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
	    final List<Method> methods = new ArrayList<Method>();
	    Class<?> klass = type;
	    while (klass != Object.class) {
	        final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));
	        for (final Method method : allMethods) {
	            if (method.isAnnotationPresent(annotation)) {
	                methods.add(method);
	            }
	        }
	        //TODO: move to the upper class in the hierarchy in search for more methods
	        klass = klass.getSuperclass();
	    }
	    return methods;
	}

	private void buildAndAddFixtureService(final Class<?> service) {
		if(!Modifier.isAbstract(service.getModifiers())) {
			final ActionAdapter docAnnotation = service.getAnnotation(ActionAdapter.class);
			fixtureServices.add(new FixtureService(service, docAnnotation.value(), docAnnotation.name()));
		}
	}

	private static Reflections buildTypeAnnotationReflection() {
		return new Reflections( new TypeAnnotationsScanner());
	}

	public Set<Method> getActionMethods() {
		return actionMethods;
	}

	public Map<Class<?>, List<Method>> getActionMethodsByClass() {
		return actionMethodsByClass;
	}

	public List<Method> getActionMethodsByClass(final Class<?> actionAdaptaterClass) {
		return Optional.ofNullable(actionMethodsByClass.get(actionAdaptaterClass)).orElse(Collections.emptyList());
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