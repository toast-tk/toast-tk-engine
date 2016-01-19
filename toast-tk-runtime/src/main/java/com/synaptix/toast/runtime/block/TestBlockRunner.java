package com.synaptix.toast.runtime.block;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.synaptix.toast.adapter.ActionAdapterCollector;
import com.synaptix.toast.adapter.FixtureService;
import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.agent.inspection.ISwingAutomationClient;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.report.TestResult.ResultKind;
import com.synaptix.toast.core.runtime.ErrorResultReceivedException;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.TestLine;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.action.item.ActionItemRegexHolder;
import com.synaptix.toast.runtime.action.item.ActionItemValueProvider;
import com.synaptix.toast.runtime.action.item.IValueHandler;
import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.runtime.bean.ActionItem.ActionTypeEnum;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;
import com.synaptix.toast.runtime.bean.CommandArgumentDescriptor;
import com.synaptix.toast.runtime.bean.TestLineDescriptor;
import com.synaptix.toast.runtime.constant.Property;
import com.synaptix.toast.runtime.utils.ArgumentHelper;

//FIXME: TOO many methods
public class TestBlockRunner implements IBlockRunner<TestBlock> {

	private static final Logger LOG = LogManager.getLogger(BlockRunnerProvider.class);

	private IActionItemRepository objectRepository;
	private ActionItemValueProvider actionItemValueProvider;
	private Injector injector;
	private List<FixtureService> fixtureApiServices;

	@Override
	public void run(TestBlock block) throws IllegalAccessException, ClassNotFoundException {
		for (TestLine line : block.getBlockLines()) {
			long startTime = System.currentTimeMillis();
			TestLineDescriptor descriptor = new TestLineDescriptor(block, line);
			TestResult result = invokeActionAdapterAction(descriptor);
			line.setExcutionTime(System.currentTimeMillis() - startTime);
			if (ResultKind.FATAL.equals(result.getResultKind())) {
				throw new IllegalAccessException(
						"Test execution stopped, due to fail fatal error: " + line + " - Failed !");
			}
			finaliseResultKind(line, result);
			line.setTestResult(result);
		}
	}

	private void finaliseResultKind(TestLine line, TestResult result) {
		if (isFailureExpected(line, result)) {
			result.setResultKind(ResultKind.SUCCESS);
		} else if (isExpectedResult(line, result)) {
			result.setResultKind(ResultKind.SUCCESS);
		}
	}

	private boolean isFailureExpected(TestLine line, TestResult result) {
		return "KO".equals(line.getExpected()) && ResultKind.FAILURE.equals(result.getResultKind());
	}

	private boolean isExpectedResult(TestLine line, TestResult result) {
		return result.getMessage() != null && line.getExpected() != null
				&& result.getMessage().equals(line.getExpected());
	}

	/**
	 * invoke the method matching the test line descriptor
	 * 
	 * @param descriptor:
	 *            descriptor of current test line
	 * @return
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private TestResult invokeActionAdapterAction(TestLineDescriptor descriptor)
			throws IllegalAccessException, ClassNotFoundException {
		TestResult result = null;
		Class<?> actionAdapter = locateActionAdapter(descriptor);
		if (hasFoundActionAdapter(actionAdapter)) {
			result = runThroughLocalActionAdapter(descriptor, actionAdapter);
			updateFatal(result, descriptor);
		} else if (isRequestFromToastStudio()) {
			result = runThroughRemoteAgent(descriptor);
			updateFatal(result, descriptor);
		} else {
			return new TestResult(String.format("Action Implementation - Not Found"), ResultKind.ERROR);
		}
		return result;
	}

	private boolean hasFoundActionAdapter(Class<?> actionAdapter) {
		return actionAdapter != null;
	}

	private boolean isRequestFromToastStudio() {
		return getClassInstance(ISwingAutomationClient.class) != null;
	}

	private void updateFatal(TestResult result, TestLineDescriptor descriptor) {
		if (descriptor.isFailFatalCommand()) {
			if (!result.isSuccess()) {
				result.setResultKind(ResultKind.FATAL);
			}
		}
	}

	/**
	 * If no class is implementing the command then process it as a custom
	 * command action request sent through network
	 * 
	 * @param descriptor
	 * @return
	 */
	private TestResult runThroughRemoteAgent(TestLineDescriptor descriptor) {
		TestResult result;
		final String command = descriptor.getActionImpl();
		result = doRemoteActionCall(command, descriptor);
		result.setContextualTestSentence(command);
		return result;
	}

	private TestResult runThroughLocalActionAdapter(
			TestLineDescriptor descriptor, 
			Class<?> actionAdapter) {
		final TestResult result;
		final String command = descriptor.getActionImpl();
		Object actionAdapterInstance = getClassInstance(actionAdapter);
		ActionCommandDescriptor actionDescriptor = findMatchingAction(command, actionAdapter);
		result = doLocalActionCall(command, actionAdapterInstance, actionDescriptor);
		return result;
	}

	/**
	 * Locate among registered ActionAdapters the best match to execute the
	 * action command
	 * 
	 * @param action
	 *            adapter kind (swing, web, service)
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	private Class<?> locateActionAdapter(TestLineDescriptor descriptor)
			throws ClassNotFoundException, IllegalAccessException {
		ActionAdapterKind actionAdapterKind = descriptor.getTestLineFixtureKind();
		String actionAdapterName = descriptor.getTestLineFixtureName();
		String actionImpl = descriptor.getActionImpl();
		Class<?> actionAdapter = null;
		actionAdapter = pickActionAdapterByNameAndKind(actionAdapterKind, actionAdapterName, actionImpl);
		if (actionAdapter == null) {
			actionAdapter = pickActionAdapterByKind(actionAdapterKind, actionImpl);
		}
		if (actionAdapter == null) {
			LOG.error("No ActionAdapter found for: {}", actionImpl);
		}
		return actionAdapter;
	}

	private Class<?> pickActionAdapterByKind(ActionAdapterKind actionAdapterKind, String actionImpl) {
		Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
		serviceClasses.addAll(collectActionAdaptersByKind(actionAdapterKind, actionImpl));
		if (serviceClasses.size() > 0) {
			if (serviceClasses.size() > 1) {
				LOG.warn("Multiple Adapters found for: {}", actionImpl);
			}
			return serviceClasses.iterator().next();
		}
		return null;
	}

	private Class<?> pickActionAdapterByNameAndKind(ActionAdapterKind actionAdapterKind, String actionAdapterName,
			String actionImpl) {
		Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
		Set<Class<?>> collectActionAdaptersByNameAndKind = collectActionAdaptersByNameAndKind(actionAdapterKind,
				actionAdapterName, actionImpl);
		serviceClasses.addAll(collectActionAdaptersByNameAndKind);
		if (serviceClasses.size() > 0) {
			if (serviceClasses.size() > 1) {
				LOG.warn("Multiple Adapters found for: {}", actionImpl);
			}
			return serviceClasses.iterator().next();
		}
		return null;
	}

	private Set<Class<?>> collectActionAdaptersByKind(ActionAdapterKind fixtureKind, String actionImpl) {
		Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
		for (FixtureService fixtureService : fixtureApiServices) {
			if (fixtureService.fixtureKind.equals(fixtureKind)) {
				ActionCommandDescriptor methodAndMatcher = findMatchingAction(actionImpl, fixtureService.clazz);
				if (methodAndMatcher != null) {
					serviceClasses.add(fixtureService.clazz);
				}
			}
		}
		return serviceClasses;
	}

	private Set<Class<?>> collectActionAdaptersByNameAndKind(ActionAdapterKind fixtureKind, String fixtureName,
			String actionImpl) {
		Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
		for (FixtureService fixtureService : fixtureApiServices) {
			if (fixtureService.fixtureKind.equals(fixtureKind) && fixtureService.fixtureName.equals(fixtureName)) {
				ActionCommandDescriptor methodAndMatcher = findMatchingAction(actionImpl, fixtureService.clazz);
				if (methodAndMatcher != null) {
					serviceClasses.add(fixtureService.clazz);
				}
			}
		}
		return serviceClasses;
	}

	private TestResult doRemoteActionCall(String command, TestLineDescriptor descriptor) {
		TestResult result;
		ISwingAutomationClient swingClient = (ISwingAutomationClient) getClassInstance(ISwingAutomationClient.class);
		swingClient.processCustomCommand(buildCommandRequest(command, descriptor));
		if (LOG.isDebugEnabled()) {
			LOG.debug("Client Plugin Mode: Delegating command interpretation to server plugins !");
		}
		result = new TestResult("Unknown command !", ResultKind.INFO);
		return result;
	}

	private TestResult doLocalActionCall(String command, Object instance, ActionCommandDescriptor execDescriptor) {
		TestResult result;
		try {
			Object[] args = buildArgumentList(execDescriptor);
			result = (TestResult) execDescriptor.method.invoke(instance, args);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			if (e instanceof ErrorResultReceivedException) {
				result = ((ErrorResultReceivedException) e).getResult();
			} else {
				result = new TestResult(ExceptionUtils.getRootCauseMessage(e), ResultKind.FAILURE);
			}
		}
		final String updatedCommand = updateCommandWithVarValues(command, execDescriptor);
		result.setContextualTestSentence(updatedCommand);
		return result;
	}

	protected Object[] buildArgumentList(ActionCommandDescriptor execDescriptor) throws Exception {
		Matcher matcher = execDescriptor.matcher;
		matcher.matches();
		int groupCount = matcher.groupCount();
		int argPos = 0;
		Object[] args = new Object[groupCount];
		for (int i = 0; i < groupCount; i++) {
			String group = matcher.group(i + 1);
			Object obj = ArgumentHelper.buildActionAdapterArgument(objectRepository, group);
			ArgumentDescriptor argumentDescriptor = execDescriptor.descriptor.arguments.get(argPos);
			if (obj instanceof String) {
				String argValue = (String) obj;
				int argIndex = execDescriptor.isMappedMethod() ? argumentDescriptor.index : i;
				IValueHandler valueHanlder = actionItemValueProvider.get(argumentDescriptor, injector);
				args[argIndex] = valueHanlder == null ? group : valueHanlder.handle(group, argValue);
			} else {
				args[i] = obj;
			}
			if (isVariable(group) 
					|| argumentDescriptor.typeEnum.equals(ActionTypeEnum.web)
					|| argumentDescriptor.typeEnum.equals(ActionTypeEnum.swing)) {
				argPos++;
			}
		}
		return args;
	}

	protected String updateCommandWithVarValues(String actionSentence, ActionCommandDescriptor execDescriptor) {
		Matcher matcher = execDescriptor.matcher;
		matcher.matches();
		String outCommand = actionSentence;
		int groupCount = matcher.groupCount();
		Object[] args = new Object[groupCount];
		for (int i = 0; i < groupCount; i++) {
			String group = matcher.group(i + 1);
			args[i] = ArgumentHelper.buildActionAdapterArgument(objectRepository, group);
			if (isVariable(args, i, group)) {
				outCommand = outCommand.replaceFirst("\\" + group + "\\b", (args[i].toString()).replace("$", "\\$"));
			}
		}
		return outCommand;
	}

	/**
	 * find the method in the action adapter class matching the command
	 * 
	 * @param actionImpl
	 * @param Action
	 *            Adapter Class
	 * @return
	 */
	public ActionCommandDescriptor findMatchingAction(final String actionImpl, final Class<?> actionAdapterClass) {
		ActionCommandDescriptor foundMethod = null;
		final List<Method> actionMethods = getActionMethods(actionAdapterClass);
		final ActionAdapter adapter = actionAdapterClass.getAnnotation(ActionAdapter.class);
		for (Method actionMethod : actionMethods) {
			final Action mainAction = actionMethod.getAnnotation(Action.class);
			foundMethod = matchMethod(actionImpl, mainAction.action(), actionMethod);
			if(foundMethod != null){
				return foundMethod;
			}else if(adapter != null && hasMapping(mainAction, adapter)){
				foundMethod = matchAgainstActionIdMapping(actionImpl, adapter.name(), actionMethod, mainAction);
				if(foundMethod != null){
					return foundMethod;
				}
			}
		}
		if (foundMethod == null && actionAdapterClass.getSuperclass() != null) {
			return findMatchingAction(actionImpl, actionAdapterClass.getSuperclass());
		}
		return foundMethod;
	}

	private boolean hasMapping(Action mainAction, ActionAdapter adapter) {
		final String actionId = mainAction.id();
		return adapter != null 
				&& hasId(mainAction)
				&& ActionSentenceMappingProvider.hasMappingForAction(adapter.name(), actionId);
	}

	private ActionCommandDescriptor matchAgainstActionIdMapping(final String actionImpl, final String adapterName,
			Method actionMethod, final Action mainAction) {
		ActionCommandDescriptor foundMethod;
		String actionMapping = ActionSentenceMappingProvider.getMappingForAction(adapterName, mainAction.id());
		String alternativeAction = buildSustituteAction(mainAction, actionMapping);
		//TODO: change mapping index position
		foundMethod = matchMethod(actionImpl, alternativeAction, actionMethod); 
		if(foundMethod != null){
			foundMethod.setIsMappedMethod(true);
			foundMethod.setActionMapping(actionMapping);
			updateArgumentIndex(foundMethod);
		}
		return foundMethod;
	}

	
	private void updateArgumentIndex(ActionCommandDescriptor foundMethod) {
		Pattern regexPattern = Pattern.compile("\\$(\\d)");
		Matcher matcher = regexPattern.matcher(foundMethod.getActionMapping());
		int pos = 0;
		List<Integer> indexes = new ArrayList<Integer>();
		while(matcher.find()){
			indexes.add(Integer.valueOf(matcher.group(1)));
		}
		for(ArgumentDescriptor aDescriptor: foundMethod.descriptor.arguments){
			aDescriptor.index = indexes.get(pos) - 1;
			pos++;
		}
	}


	private class ActionIndex{
		String item;
		Integer start;
		Integer end;
	}
	
	protected String buildSustituteAction(Action mainAction, String mapping) {
		Pattern regexPattern = ActionItemRegexHolder.getFullMetaPattern();
		String action = mainAction.action();
		Matcher matcher = regexPattern.matcher(action);
		List<ActionIndex> actionItems = new ArrayList<>();
		while(matcher.find()){
			ActionIndex index = new ActionIndex();
			index.item = matcher.group(0);
			index.start = matcher.start();
			index.end = matcher.end();
			actionItems.add(index);
		}
		for (ActionIndex actionItem : actionItems) {
			String index = "$"+(actionItems.indexOf(actionItem)+1);
			mapping = mapping.replace(index, actionItem.item);
		}
		return mapping;
	}

	private ActionCommandDescriptor matchMethod(
			final String actionImpl,
			final String actionTpl,
			Method actionMethod) {
		CommandArgumentDescriptor commandDescriptor = ArgumentHelper.convertActionSentenceToRegex(actionTpl);
		ActionCommandDescriptor actionCommandWrapper = null;
		Pattern regexPattern = Pattern.compile(commandDescriptor.regex);
		Matcher methodMatcher = regexPattern.matcher(actionImpl);
		if (methodMatcher.matches()) {
			Pattern varRegexPattern = ActionItemRegexHolder.getVarPattern();
			Matcher varMatcher = varRegexPattern.matcher(actionImpl);
			int pos = 0;
			while (varMatcher.find()) {
				String varName = varMatcher.group(1);
				ArgumentDescriptor argumentDescriptor = commandDescriptor.arguments.get(pos);
				argumentDescriptor.varName = varName;
				if (argumentDescriptor.name == null) {
					Parameter parameter = actionMethod.getParameters()[pos];
					argumentDescriptor.name = parameter.getType().getName();
				}
				pos++;
			}
			actionCommandWrapper = new ActionCommandDescriptor(actionMethod, methodMatcher, commandDescriptor);
		}
		return actionCommandWrapper;
	}

	private boolean hasId(Action action) {
		return !StringUtils.isEmpty(action.id());
	}

	private List<Method> getActionMethods(final Class<?> actionAdapterClass) {
		List<Method> actionMethods = new ArrayList<>();
		Method[] methods = actionAdapterClass.getMethods();
		for (Method method : methods) {
			Action action = method.getAnnotation(Action.class);
			if (action != null) {
				actionMethods.add(method);
			}
		}
		return actionMethods;
	}

	private boolean isVariable(String group) {
		return group.startsWith("$");
	}

	private boolean isVariable(Object[] args, int i, String group) {
		return isVariable(group) && args[i] != null && !group.contains(Property.DEFAULT_PARAM_SEPARATOR);
	}

	private CommandRequest buildCommandRequest(String command, TestLineDescriptor descriptor) {
		final CommandRequest commandRequest;
		switch (descriptor.getTestLineFixtureKind()) {
		case service:
			commandRequest = new CommandRequest.CommandRequestBuilder(null).ofType(ActionAdapterKind.service.name())
					.asCustomCommand(command).build();
			break;
		default:
			commandRequest = new CommandRequest.CommandRequestBuilder(null).asCustomCommand(command).build();
			break;
		}
		return commandRequest;
	}

	private Object getClassInstance(Class<?> clz) {
		if (injector != null) {
			try {
				return injector.getInstance(clz);
			} catch (ConfigurationException _ce) {
				return null;
			}
		}
		return null;
	}

	public void setInjector(Injector injector) {
		this.injector = injector;
		setActionItemValueProvider(injector.getInstance(ActionItemValueProvider.class));
		setObjectRepository(injector.getInstance(IActionItemRepository.class));
		this.fixtureApiServices = ActionAdapterCollector.listAvailableServicesByInjection(injector);
	}

	public void setObjectRepository(IActionItemRepository repository) {
		this.objectRepository = repository;
	}
	
	public void setActionItemValueProvider(ActionItemValueProvider repository) {
		this.actionItemValueProvider = repository;
	}
}
