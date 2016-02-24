package com.synaptix.toast.runtime.block.locator;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.synaptix.toast.adapter.FixtureService;
import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;
import com.synaptix.toast.runtime.action.item.ActionItemRegexHolder;
import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;
import com.synaptix.toast.runtime.bean.CommandArgumentDescriptor;
import com.synaptix.toast.runtime.bean.TestLineDescriptor;
import com.synaptix.toast.runtime.block.ActionSentenceMappingProvider;
import com.synaptix.toast.runtime.utils.ArgumentHelper;

public class ActionAdaptaterLocator {

	private static final Pattern REGEX_PATTERN = Pattern.compile("\\$(\\d)");

	private static final Logger LOG = LogManager.getLogger(ActionAdaptaterLocator.class);

	private final TestLineDescriptor testLineDescriptor;

	private final Injector injector;
	
	private FixtureServicesLocator fixtureServicesLocator;

	private ActionCommandDescriptor actionCommandDescriptor;
	
	private Object instance;
	
	private Class<?> actionAdaptaterClass;
	
	public ActionAdaptaterLocator(
		final TestLineDescriptor testLineDescriptor,
		final Injector injector
	) {
		this.testLineDescriptor = Objects.requireNonNull(testLineDescriptor, "ActionAdaptaterLocator.testLineDescriptor is null");
		this.injector = Objects.requireNonNull(injector, "ActionAdaptaterLocator.injector is null");
		this.fixtureServicesLocator = FixtureServicesLocator.getFixtureServicesLocator(injector);
	}

	private Class<?> locateActionAdapter() {
		Class<?> actionAdapter = pickActionAdapterByNameAndKind();
		if(actionAdapter == null) {
			actionAdapter = pickActionAdapterByKind();
		}
		if(actionAdapter == null) {
			LOG.error("No ActionAdapter found for: {}", testLineDescriptor.getActionImpl());
		}
		instance = getClassInstance(actionAdapter);
		
		return actionAdapter;
	}
	
	private Object getClassInstance(final Class<?> clz) {
		if(injector != null && clz != null) {
			try {
				return injector.getInstance(clz);
			} 
			catch(final ConfigurationException e) {
				LOG.error(e.getMessage(), e);
				return null;
			}
		}
		return null;
	}

	private Class<?> pickActionAdapterByKind() {
		final Set<Class<?>> serviceClasses = new HashSet<>(collectActionAdaptersByKind());
		final int nbServiceClasses = serviceClasses.size();
		if(nbServiceClasses > 1) {
			LOG.warn("Multiple Adapters found for: {}", testLineDescriptor.getActionImpl());
		}
		else if(nbServiceClasses > 0) {
			return serviceClasses.iterator().next();
		}
		return null;
	}

	private Class<?> pickActionAdapterByNameAndKind() {
		final Set<Class<?>> serviceClasses = new HashSet<>(collectActionAdaptersByNameAndKind());
		final int nbServiceClasse = serviceClasses.size(); 
		if(nbServiceClasse > 0) {
			if(nbServiceClasse > 1) {
				LOG.warn("Multiple Adapters found for: {}", testLineDescriptor.getActionImpl());
			}
			return serviceClasses.iterator().next();
		}
		return null;
	}

	private Set<Class<?>> collectActionAdaptersByKind() {
		final Set<Class<?>> serviceClasses = new HashSet<>(fixtureServicesLocator.getFixtureApiServices().size());
		fixtureServicesLocator.getFixtureApiServices().stream().forEach(fixtureService -> addFixtureByFixtureKind(testLineDescriptor.getTestLineFixtureKind(), serviceClasses, fixtureService));
		return serviceClasses;
	}

	private void addFixtureByFixtureKind(
		final ActionAdapterKind fixtureKind,
		final Set<Class<?>> serviceClasses,
		final FixtureService fixtureService
	) {
		if(sameFixtureKind(fixtureKind, fixtureService)) {
			final ActionCommandDescriptor methodAndMatcher = findMatchingAction(fixtureService.clazz);
			if(methodAndMatcher != null) {
				serviceClasses.add(fixtureService.clazz);
			}
		}
	}

	private static boolean sameFixtureKind(
		final ActionAdapterKind fixtureKind,
		final FixtureService fixtureService
	) {
		return fixtureService.fixtureKind.equals(fixtureKind);
	}

	private Set<Class<?>> collectActionAdaptersByNameAndKind() {
		final Set<Class<?>> serviceClasses = new HashSet<>(fixtureServicesLocator.getFixtureApiServices().size());
		fixtureServicesLocator.getFixtureApiServices().stream().forEach(fixtureService -> addFixtureByFixtureKindAndName(serviceClasses, fixtureService));
		return serviceClasses;
	}

	private void addFixtureByFixtureKindAndName(
		final Set<Class<?>> serviceClasses,
		final FixtureService fixtureService
	) {
		if(sameKindAndName(fixtureService)) {
			final ActionCommandDescriptor methodAndMatcher = findMatchingAction(fixtureService.clazz);
			if(methodAndMatcher != null) {
				serviceClasses.add(fixtureService.clazz);
			}
		}
	}

	private boolean sameKindAndName(final FixtureService fixtureService) {
		return fixtureService.fixtureKind.equals(testLineDescriptor.getTestLineFixtureKind()) && fixtureService.fixtureName.equals(testLineDescriptor.getTestLineFixtureName());
	}

	public ActionCommandDescriptor findActionCommandDescriptor() {
		return actionCommandDescriptor == null ? actionCommandDescriptor = findMatchingAction(actionAdaptaterClass = locateActionAdapter()) : actionCommandDescriptor;
	}
	
	public ActionCommandDescriptor getActionCommandDescriptor() {
		return actionCommandDescriptor;
	}
	
	public Object getInstance() {
		return instance;
	}

	public TestLineDescriptor getTestLineDescriptor() {
		return testLineDescriptor;
	}
	
	public Class<?> getActionAdaptaterClass() {
		return actionAdaptaterClass;
	}
	
	private ActionCommandDescriptor findMatchingAction(final Class<?> actionAdapterClass) {
		final List<Method> actionMethods = getActionMethods(actionAdapterClass);
		final ActionAdapter adapter = actionAdapterClass.getAnnotation(ActionAdapter.class);
		for(final Method actionMethod : actionMethods) {
			final Action mainAction = actionMethod.getAnnotation(Action.class);
			ActionCommandDescriptor foundMethod = matchMethod(testLineDescriptor.getActionImpl(), mainAction.action(), actionMethod);
			if(foundMethod != null) {
				return foundMethod;
			}
			else if(adapter != null && hasMapping(mainAction, adapter)) {
				foundMethod = matchAgainstActionIdMapping(testLineDescriptor.getActionImpl(), adapter.name(), actionMethod, mainAction);
				if (foundMethod != null) {
					return foundMethod;
				}
			}
		}
		if(actionAdapterClass.getSuperclass() != null) {
			return findMatchingAction(actionAdapterClass.getSuperclass());
		}
		return null;
	}

	private static ActionCommandDescriptor matchAgainstActionIdMapping(
		final String actionImpl, 
		final String adapterName,
		final Method actionMethod, 
		final Action mainAction
	) {
		final String actionMapping = ActionSentenceMappingProvider.getMappingForAction(adapterName, mainAction.id());
		final String alternativeAction = buildSubstituteAction(mainAction, actionMapping);
		//TODO: change mapping index position
		final ActionCommandDescriptor foundMethod = matchMethod(actionImpl, alternativeAction, actionMethod);
		if(foundMethod != null) {
			foundMethod.setIsMappedMethod(true);
			foundMethod.setActionMapping(actionMapping);
			updateArgumentIndex(foundMethod);
		}
		return foundMethod;
	}

	private static void updateArgumentIndex(final ActionCommandDescriptor foundMethod) {
		final Matcher matcher = REGEX_PATTERN.matcher(foundMethod.getActionMapping());
		int pos = 0;
		final List<Integer> indexes = buildIndexes(matcher);
		for(final ArgumentDescriptor aDescriptor : foundMethod.descriptor.arguments) {
			aDescriptor.index = indexes.get(pos++) - 1;
		}
	}

	private static List<Integer> buildIndexes(final Matcher matcher) {
		final List<Integer> indexes = new ArrayList<>();
		while(matcher.find()) {
			indexes.add(Integer.valueOf(matcher.group(1)));
		}
		return indexes;
	}

	private static String buildSubstituteAction(
		final Action mainAction, 
		String mapping
	) {
		final Matcher matcher = ActionItemRegexHolder.getFullMetaPattern().matcher(mainAction.action());
		final List<ActionIndex> actionItems = buildActionIndexes(matcher);
		for(final ActionIndex actionItem : actionItems) {
			final String index = "$" + (actionItems.indexOf(actionItem) + 1);
			mapping = mapping.replace(index, actionItem.item);
		}
		return mapping;
	}

	private static List<ActionIndex> buildActionIndexes(final Matcher matcher) {
		final List<ActionIndex> actionItems = new ArrayList<>();
		while(matcher.find()) {
			actionItems.add(new ActionIndex(matcher.group(0), matcher.start(), matcher.end()));
		}
		return actionItems;
	}

	private static boolean hasMapping(
		final Action mainAction, 
		final ActionAdapter adapter
	) {
		return 
				adapter != null
				&& 
				hasId(mainAction)
				&& 
				ActionSentenceMappingProvider.hasMappingForAction(adapter.name(), mainAction.id())
				;
	}

	private static boolean hasId(final Action action) {
		return !StringUtils.isEmpty(action.id());
	}

	private static List<Method> getActionMethods(final Class<?> actionAdapterClass) {
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

	private static ActionCommandDescriptor matchMethod(
		final String actionImpl,
		final String actionTpl,
		final Method actionMethod
	) {
		final CommandArgumentDescriptor commandDescriptor = ArgumentHelper.convertActionSentenceToRegex(actionTpl);
		final Matcher methodMatcher = buildMethodMatcher(actionImpl, commandDescriptor);
		if(methodMatcher.matches()) {
			final Matcher varMatcher = buildVarMatcher(actionImpl);
			int pos = 0;
			while(varMatcher.find()) {
				final ArgumentDescriptor argumentDescriptor = commandDescriptor.arguments.get(pos);
				setVarName(varMatcher, argumentDescriptor);
				setParameterClassName(actionMethod, pos, argumentDescriptor);
				++pos;
			}
			return new ActionCommandDescriptor(actionMethod, methodMatcher, commandDescriptor);
		}
		return null;
	}

	private static void setVarName(
		final Matcher varMatcher,
		final ArgumentDescriptor argumentDescriptor
	) {
		argumentDescriptor.varName = varMatcher.group(1);
	}

	private static void setParameterClassName(
		final Method actionMethod,
		final int pos, 
		final ArgumentDescriptor argumentDescriptor
	) {
		if(argumentDescriptor.name == null) {
			final Parameter parameter = actionMethod.getParameters()[pos];
			argumentDescriptor.name = parameter.getType().getName();
		}
	}

	private static Matcher buildVarMatcher(final String actionImpl) {
		final Pattern varRegexPattern = ActionItemRegexHolder.getVarPattern();
		return varRegexPattern.matcher(actionImpl);
	}

	private static Matcher buildMethodMatcher(
		final String actionImpl,
		final CommandArgumentDescriptor commandDescriptor
	) {
		final Pattern regexPattern = Pattern.compile(commandDescriptor.regex);
		return regexPattern.matcher(actionImpl);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(testLineDescriptor);
	}
	
	@Override
	public boolean equals(final Object obj) {
		return obj instanceof ActionAdaptaterLocator ? Objects.equals(testLineDescriptor, ((ActionAdaptaterLocator) obj).testLineDescriptor) : false; 
	}
}