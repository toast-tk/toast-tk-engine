package com.synaptix.toast.runtime.block;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.synaptix.toast.adapter.ActionAdapterCollector;
import com.synaptix.toast.adapter.FixtureService;
import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;
import com.synaptix.toast.core.report.FailureResult;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.runtime.ErrorResultReceivedException;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult.ResultKind;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.TestLine;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.action.item.ActionItemRegexHolder;
import com.synaptix.toast.runtime.action.item.ActionItemValueProvider;
import com.synaptix.toast.runtime.action.item.IValueHandler;
import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;
import com.synaptix.toast.runtime.bean.CommandArgumentDescriptor;
import com.synaptix.toast.runtime.bean.TestLineDescriptor;
import com.synaptix.toast.runtime.constant.Property;
import com.synaptix.toast.runtime.utils.ArgumentHelper;

//FIXME: TOO many methods
public class TestBlockRunner implements IBlockRunner<TestBlock> {

	private static final Logger LOG = LogManager.getLogger(BlockRunnerProvider.class);
	
	private static final Pattern REGEX_PATTERN = Pattern.compile("\\$(\\d)");

	private IActionItemRepository objectRepository;

	private ActionItemValueProvider actionItemValueProvider;

	private Injector injector;

	private List<FixtureService> fixtureApiServices;

	@Override
	public void run(final TestBlock block) throws IllegalAccessException, ClassNotFoundException {
		block.getBlockLines().stream().forEach(line -> invokeTestAndAddResult(block, line));
	}

	private void invokeTestAndAddResult(
		final TestBlock block, 
		final TestLine line
	) throws IllegalAccessException, ClassNotFoundException {
		final long startTime = System.currentTimeMillis();
		final TestLineDescriptor descriptor = new TestLineDescriptor(block, line);
		final TestResult result = invokeActionAdapterAction(descriptor);
		line.setExcutionTime(System.currentTimeMillis() - startTime);
		if (result.isFatal()) {
			throw new IllegalAccessException("Test execution stopped, due to fail fatal error: " + line + " - Failed !");
		}
		finaliseResultKind(line, result);
		line.setTestResult(result);
	}

	private static void finaliseResultKind(
		final TestLine line, 
		final ITestResult result
	) {
		if(isFailureExpected(line, result) || isExpectedResult(line, result)) {
			result.setResultKind(ResultKind.SUCCESS);
		} 
	}

	private static boolean isFailureExpected(
		final TestLine line, 
		final ITestResult result
	) {
		return "KO".equals(line.getExpected()) && ResultKind.FAILURE.equals(result.getResultKind());
	}

	private static boolean isExpectedResult(
		final TestLine line, 
		final ITestResult result
	) {
		return result.getMessage() != null && line.getExpected() != null && result.getMessage().equals(line.getExpected());
	}

	/**
	 * invoke the method matching the test line descriptor
	 *
	 * @param descriptor: descriptor of current test line
	 * @return
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private TestResult invokeActionAdapterAction(final TestLineDescriptor descriptor) throws IllegalAccessException, ClassNotFoundException {
		final Class<?> actionAdapter = locateActionAdapter(descriptor);
		if(hasFoundActionAdapter(actionAdapter)) {
			final TestResult result = runThroughLocalActionAdapter(descriptor, actionAdapter);
			updateFatal(result, descriptor);
			return result; 
		} 
		return new FailureResult(String.format("Action Implementation - Not Found"));
	}

	private static boolean hasFoundActionAdapter(final Class<?> actionAdapter) {
		return actionAdapter != null;
	}

	private static void updateFatal(
		final ITestResult result, 
		final TestLineDescriptor descriptor
	) {
		if(descriptor.isFailFatalCommand()) {
			if (!result.isSuccess()) {
				result.setResultKind(ResultKind.FATAL);
			}
		}
	}

	private TestResult runThroughLocalActionAdapter(
		final TestLineDescriptor descriptor,
		final Class<?> actionAdapter
	) {
		final String command = descriptor.getActionImpl();
		final Object actionAdapterInstance = getClassInstance(actionAdapter);
		final ActionCommandDescriptor actionDescriptor = findMatchingAction(command, actionAdapter);
		return doLocalActionCall(command, actionAdapterInstance, actionDescriptor);
	}

	/**
	 * Locate among registered ActionAdapters the best match to execute the
	 * action command
	 *
	 * @param action adapter kind (swing, web, service)
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	private Class<?> locateActionAdapter(final TestLineDescriptor descriptor) throws ClassNotFoundException, IllegalAccessException {
		final ActionAdapterKind actionAdapterKind = descriptor.getTestLineFixtureKind();
		final String actionAdapterName = descriptor.getTestLineFixtureName();
		final String actionImpl = descriptor.getActionImpl();
		Class<?> actionAdapter = pickActionAdapterByNameAndKind(actionAdapterKind, actionAdapterName, actionImpl);
		if(actionAdapter == null) {
			actionAdapter = pickActionAdapterByKind(actionAdapterKind, actionImpl);
		}
		if(actionAdapter == null) {
			LOG.error("No ActionAdapter found for: {}", actionImpl);
		}
		return actionAdapter;
	}

	private Class<?> pickActionAdapterByKind(
		final ActionAdapterKind actionAdapterKind, 
		final String actionImpl
	) {
		final Set<Class<?>> serviceClasses = new HashSet<>(collectActionAdaptersByKind(actionAdapterKind, actionImpl));
		final int nbServiceClasses = serviceClasses.size();
		if(nbServiceClasses > 0) {
			if(nbServiceClasses > 1) {
				LOG.warn("Multiple Adapters found for: {}", actionImpl);
			}
			return serviceClasses.iterator().next();
		}
		return null;
	}

	private Class<?> pickActionAdapterByNameAndKind(
		final ActionAdapterKind actionAdapterKind, 
		final String actionAdapterName,
		final String actionImpl
	) {
		final Set<Class<?>> serviceClasses = new HashSet<>(collectActionAdaptersByNameAndKind(actionAdapterKind, actionAdapterName, actionImpl));
		final int nbServiceClasse = serviceClasses.size(); 
		if(nbServiceClasse > 0) {
			if(nbServiceClasse > 1) {
				LOG.warn("Multiple Adapters found for: {}", actionImpl);
			}
			return serviceClasses.iterator().next();
		}
		return null;
	}

	private Set<Class<?>> collectActionAdaptersByKind(
		final ActionAdapterKind fixtureKind, 
		final String actionImpl
	) {
		final Set<Class<?>> serviceClasses = new HashSet<>(fixtureApiServices.size());
		fixtureApiServices.stream().forEach(fixtureService -> addFixtureByFixtureKind(fixtureKind, actionImpl, serviceClasses, fixtureService));
		return serviceClasses;
	}

	private void addFixtureByFixtureKind(
		final ActionAdapterKind fixtureKind,
		final String actionImpl, 
		final Set<Class<?>> serviceClasses,
		final FixtureService fixtureService
	) {
		if(fixtureService.fixtureKind.equals(fixtureKind)) {
			final ActionCommandDescriptor methodAndMatcher = findMatchingAction(actionImpl, fixtureService.clazz);
			if(methodAndMatcher != null) {
				serviceClasses.add(fixtureService.clazz);
			}
		}
	}

	private Set<Class<?>> collectActionAdaptersByNameAndKind(
		final ActionAdapterKind fixtureKind, 
		final String fixtureName,
		final String actionImpl
	) {
		final Set<Class<?>> serviceClasses = new HashSet<>(fixtureApiServices.size());
		fixtureApiServices.stream().forEach(fixtureService -> addFixtureByFixtureKindAndName(fixtureKind, fixtureName, actionImpl, serviceClasses, fixtureService));
		return serviceClasses;
	}

	private void addFixtureByFixtureKindAndName(
			final ActionAdapterKind fixtureKind, final String fixtureName,
			final String actionImpl, final Set<Class<?>> serviceClasses,
			final FixtureService fixtureService) {
		if (fixtureService.fixtureKind.equals(fixtureKind) && fixtureService.fixtureName.equals(fixtureName)) {
			final ActionCommandDescriptor methodAndMatcher = findMatchingAction(actionImpl, fixtureService.clazz);
			if(methodAndMatcher != null) {
				serviceClasses.add(fixtureService.clazz);
			}
		}
	}
	
	private TestResult doLocalActionCall(
		final String command, 
		final Object instance, 
		final ActionCommandDescriptor execDescriptor
	) {
		TestResult result = null;
		try {
			result = (TestResult) execDescriptor.method.invoke(instance, buildArgumentList(execDescriptor));
		} 
		catch(final Exception e) {
			LOG.error(e.getMessage(), e);
			if (e instanceof ErrorResultReceivedException) {
				result = ((ErrorResultReceivedException) e).getResult();
			} 
			else {
				result = new FailureResult(ExceptionUtils.getRootCauseMessage(e));
			}
		}
		final String updatedCommand = updateCommandWithVarValues(command, execDescriptor);
		if(result != null) {
			result.setContextualTestSentence(updatedCommand);
		}
		return result;
	}

	protected Object[] buildArgumentList(
		final ActionCommandDescriptor execDescriptor
	) throws Exception {
		final int groupCount = execDescriptor.matcher.groupCount();
		final Object[] args = new Object[groupCount];
		for(int index = 0; index < groupCount; ++index) {
			final String group = execDescriptor.matcher.group(index + 1);
			final Object obj = ArgumentHelper.buildActionAdapterArgument(objectRepository, group);
			final ArgumentDescriptor argumentDescriptor = execDescriptor.descriptor.arguments.get(index);
			if(obj instanceof String) {
				final String argValue = (String) obj;
				final int argIndex = execDescriptor.isMappedMethod() ? argumentDescriptor.index : index;
				final IValueHandler valueHandlder = actionItemValueProvider.get(argumentDescriptor, injector);
				final Object argument = valueHandlder == null ? group : valueHandlder.handle(group, argValue);
				if(argument == null) {
					throw new ScriptException("Element " + argValue + " was not defined");
				}
				args[argIndex] = argument;
			} 
			else {
				args[index] = obj;
			}
		}
		return args;
	}

	protected String updateCommandWithVarValues(
		final String actionSentence, 
		final ActionCommandDescriptor execDescriptor
	) {
		final Matcher matcher = execDescriptor.matcher;
		matcher.matches();
		String outCommand = actionSentence;
		final int groupCount = matcher.groupCount();
		final Object[] args = new Object[groupCount];
		for(int i = 0; i < groupCount; i++) {
			final String group = matcher.group(i + 1);
			args[i] = ArgumentHelper.buildActionAdapterArgument(objectRepository, group);
			if(isVariable(args, i, group)) {
				outCommand = outCommand.replaceFirst("\\" + group + "\\b", (args[i].toString()).replace("$", "\\$"));
			}
		}
		return outCommand;
	}

	/**
	 * Find the method in the action adapter class matching the command
	 */
	public ActionCommandDescriptor findMatchingAction(
		final String actionImpl, 
		final Class<?> actionAdapterClass
	) {
		final List<Method> actionMethods = getActionMethods(actionAdapterClass);
		final ActionAdapter adapter = actionAdapterClass.getAnnotation(ActionAdapter.class);
		for(final Method actionMethod : actionMethods) {
			final Action mainAction = actionMethod.getAnnotation(Action.class);
			ActionCommandDescriptor foundMethod = matchMethod(actionImpl, mainAction.action(), actionMethod);
			if(foundMethod != null) {
				return foundMethod;
			}
			else if(adapter != null && hasMapping(mainAction, adapter)) {
				foundMethod = matchAgainstActionIdMapping(actionImpl, adapter.name(), actionMethod, mainAction);
				if (foundMethod != null) {
					return foundMethod;
				}
			}
		}
		if (actionAdapterClass.getSuperclass() != null) {
			return findMatchingAction(actionImpl, actionAdapterClass.getSuperclass());
		}
		return null;
	}

	private boolean hasMapping(
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

	private ActionCommandDescriptor matchAgainstActionIdMapping(
		final String actionImpl, 
		final String adapterName,
		final Method actionMethod, 
		final Action mainAction
	) {
		final String actionMapping = ActionSentenceMappingProvider.getMappingForAction(adapterName, mainAction.id());
		final String alternativeAction = buildSustituteAction(mainAction, actionMapping);
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
		final List<Integer> indexes = new ArrayList<>();
		while(matcher.find()) {
			indexes.add(Integer.valueOf(matcher.group(1)));
		}
		for(final ArgumentDescriptor aDescriptor : foundMethod.descriptor.arguments) {
			aDescriptor.index = indexes.get(pos) - 1;
			++pos;
		}
	}

	class ActionIndex {

		String item;

		Integer start;

		Integer end;
		
		public ActionIndex(
			final String item,
			final Integer start,
			final Integer end
		) {
			this.item = item;
			this.start = start;
			this.end = end;
		}
	}

	protected String buildSustituteAction(
		final Action mainAction, 
		String mapping
	) {
		final Matcher matcher = ActionItemRegexHolder.getFullMetaPattern().matcher(mainAction.action());
		final List<ActionIndex> actionItems = new ArrayList<>();
		while(matcher.find()) {
			actionItems.add(new ActionIndex(matcher.group(0), matcher.start(), matcher.end()));
		}
		for(final ActionIndex actionItem : actionItems) {
			final String index = "$" + (actionItems.indexOf(actionItem) + 1);
			mapping = mapping.replace(index, actionItem.item);
		}
		return mapping;
	}

	private static ActionCommandDescriptor matchMethod(
		final String actionImpl,
		final String actionTpl,
		final Method actionMethod
	) {
		final CommandArgumentDescriptor commandDescriptor = ArgumentHelper.convertActionSentenceToRegex(actionTpl);
		final Pattern regexPattern = Pattern.compile(commandDescriptor.regex);
		final Matcher methodMatcher = regexPattern.matcher(actionImpl);
		if(methodMatcher.matches()) {
			final Pattern varRegexPattern = ActionItemRegexHolder.getVarPattern();
			final Matcher varMatcher = varRegexPattern.matcher(actionImpl);
			int pos = 0;
			while(varMatcher.find()) {
				final ArgumentDescriptor argumentDescriptor = commandDescriptor.arguments.get(pos);
				argumentDescriptor.varName = varMatcher.group(1);
				if(argumentDescriptor.name == null) {
					final Parameter parameter = actionMethod.getParameters()[pos];
					argumentDescriptor.name = parameter.getType().getName();
				}
				++pos;
			}
			return new ActionCommandDescriptor(actionMethod, methodMatcher, commandDescriptor);
		}
		return null;
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

	private static boolean isVariable(String group) {
		return group.startsWith("$");
	}

	private static boolean isVariable(Object[] args, int i, String group) {
		return isVariable(group) && args[i] != null && !group.contains(Property.DEFAULT_PARAM_SEPARATOR);
	}

	private Object getClassInstance(final Class<?> clz) {
		if (injector != null) {
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

	@Override
	public void setInjector(final Injector injector) {
		this.injector = injector;
		this.actionItemValueProvider = injector.getInstance(ActionItemValueProvider.class);
		this.objectRepository = injector.getInstance(IActionItemRepository.class);
		this.fixtureApiServices = ActionAdapterCollector.listAvailableServicesByInjection(injector);
	}

	public void setObjectRepository(IActionItemRepository repository) {
		this.objectRepository = repository;
	}

	public void setActionItemValueProvider(ActionItemValueProvider repository) {
		this.actionItemValueProvider = repository;
	}
}