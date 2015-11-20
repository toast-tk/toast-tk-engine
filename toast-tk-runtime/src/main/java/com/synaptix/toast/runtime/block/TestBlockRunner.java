package com.synaptix.toast.runtime.block;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.synaptix.toast.adapter.ActionAdapterCollector;
import com.synaptix.toast.adapter.FixtureService;
import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.agent.inspection.ISwingAutomationClient;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.report.TestResult.ResultKind;
import com.synaptix.toast.core.runtime.ErrorResultReceivedException;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.TestLine;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;
import com.synaptix.toast.runtime.bean.CommandArgumentDescriptor;
import com.synaptix.toast.runtime.bean.TestLineDescriptor;
import com.synaptix.toast.runtime.constant.Property;
import com.synaptix.toast.runtime.utils.ArgumentHelper;

public class TestBlockRunner implements IBlockRunner<TestBlock> {

	private static final Logger LOG = LogManager.getLogger(BlockRunnerProvider.class);

	@Inject
	private IActionItemRepository objectRepository;
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
				throw new IllegalAccessException("Test execution stopped, due to fail fatal error: "+ line + " - Failed !");
			}
			finaliseResultKind(line, result);
			line.setTestResult(result);
		}
	}

	private void finaliseResultKind(TestLine line, TestResult result) {
		if (isFailureExpected(line, result)) {
			result.setResultKind(ResultKind.SUCCESS);
		} 
		else if (isExpectedResult(line, result)) {
			result.setResultKind(ResultKind.SUCCESS);
		}
	}

	private boolean isFailureExpected(TestLine line, TestResult result) {
		return "KO".equals(line.getExpected())
				&& ResultKind.FAILURE.equals(result.getResultKind());
	}

	private boolean isExpectedResult(TestLine line, TestResult result) {
		return result.getMessage() != null
				&& line.getExpected() != null
				&& result.getMessage().equals(line.getExpected());
	}

	/**
	 * invoke the method matching the test line descriptor
	 * 
	 * @param descriptor: descriptor of current test line
	 * @return
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private TestResult invokeActionAdapterAction(
			TestLineDescriptor descriptor) throws IllegalAccessException,
			ClassNotFoundException {
		TestResult result = null;
		Class<?> actionAdapter = locateActionAdapter(descriptor);
		if (hasFoundActionAdapter(actionAdapter)) {
			result = runThroughLocalActionAdapter(descriptor, actionAdapter);
			updateFatal(result, descriptor);
		} 
		else if (isRequestFromToastStudio()) {
			result = runThroughRemoteAgent(descriptor);
			updateFatal(result, descriptor);
		} 
		else {
			return new TestResult(String.format("Action Implementation - Not Found"),ResultKind.ERROR);
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
	 * If no class is implementing the command then
	 * process it as a custom command action request sent through Kryo
	 * 
	 * @param descriptor
	 * @return
	 */
	private TestResult runThroughRemoteAgent(TestLineDescriptor descriptor) {
		TestResult result;
		final String command = descriptor.getCommand();
		result = doRemoteActionCall(command, descriptor);
		result.setContextualTestSentence(command);
		return result;
	}

	private TestResult runThroughLocalActionAdapter(
			TestLineDescriptor descriptor, Class<?> actionAdapter) {
		TestResult result;
		final String command = descriptor.getCommand();
		LOG.info(actionAdapter + " : " + command);
		Object connector = getClassInstance(actionAdapter);
		ActionCommandDescriptor commandMethodImpl = findMethodInClass(command, actionAdapter);
		if (commandMethodImpl == null) {
			commandMethodImpl = findMethodInClass(command, actionAdapter);
		}
		result = doLocalActionCall(command, connector, commandMethodImpl);
		return result;
	}

	/**
	 * Locate among registered ActionAdapters the best match to execute
	 * the action command
	 * 
	 * @param action adapter kind (swing, web, service)
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	private Class<?> locateActionAdapter(TestLineDescriptor descriptor) throws ClassNotFoundException,
			IllegalAccessException {
		ActionAdapterKind actionAdapterKind = descriptor.getTestLineFixtureKind();
		String actionAdapterName = descriptor.getTestLineFixtureName(); 
		String command = descriptor.getCommand();
		Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
		
		Set<Class<?>> collectActionAdaptersByNameAndKind = collectActionAdaptersByNameAndKind(actionAdapterKind, actionAdapterName, command);
		serviceClasses.addAll(collectActionAdaptersByNameAndKind);
		if (serviceClasses.size() > 0) {
			if(serviceClasses.size() > 1){
				LOG.warn("Multiple Services of same name and kind found implementing the same command: {}", command);
			}
			return serviceClasses.iterator().next();
		}
		
		serviceClasses.addAll(collectActionAdaptersByKind(actionAdapterKind, command));
		if (serviceClasses.size() > 0) {
			if(serviceClasses.size() > 1){
				LOG.warn("Multiple Services of same kind found implementing the same command: {}", command);
			}
			return serviceClasses.iterator().next();
		}
		
		LOG.error("No Connector found for command: {}", command);
		return null;
	}

	private Set<Class<?>> collectActionAdaptersByKind(
			ActionAdapterKind fixtureKind,
			String command) {
		Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
		for (FixtureService fixtureService : fixtureApiServices) {
			if (fixtureService.fixtureKind.equals(fixtureKind)) {
				ActionCommandDescriptor methodAndMatcher = findMethodInClass(command, fixtureService.clazz);
				if (methodAndMatcher != null) {
					serviceClasses.add(fixtureService.clazz);
				}
			}
		}
		return serviceClasses;
	}

	private Set<Class<?>> collectActionAdaptersByNameAndKind(
			ActionAdapterKind fixtureKind, 
			String fixtureName, 
			String command) {
		Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
		for (FixtureService fixtureService : fixtureApiServices) {
			if (fixtureService.fixtureKind.equals(fixtureKind) && fixtureService.fixtureName.equals(fixtureName)) {
				ActionCommandDescriptor methodAndMatcher = findMethodInClass(command, fixtureService.clazz);
				if (methodAndMatcher != null) {
					serviceClasses.add(fixtureService.clazz);
				}
			}
		}
		return serviceClasses;
	}

	private TestResult doRemoteActionCall(String command,
			TestLineDescriptor descriptor) {
		TestResult result;
		ISwingAutomationClient swingClient = (ISwingAutomationClient) getClassInstance(ISwingAutomationClient.class);
		swingClient.processCustomCommand(buildCommandRequest(command,descriptor));
		if (LOG.isDebugEnabled()) {
			LOG.debug("Client Plugin Mode: Delegating command interpretation to server plugins !");
		}
		result = new TestResult("Unknown command !", ResultKind.INFO);
		return result;
	}

	private TestResult doLocalActionCall(String command, Object instance,
			ActionCommandDescriptor execDescriptor) {
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

	protected Object[] buildArgumentList(
			ActionCommandDescriptor execDescriptor) throws Exception {
		Matcher matcher = execDescriptor.matcher;
		matcher.matches();
		int groupCount = matcher.groupCount();
		int argPos = 0;
		Object[] args = new Object[groupCount];
		for (int i = 0; i < groupCount; i++) {
			String group = matcher.group(i + 1);
			Object obj = ArgumentHelper.buildActionAdapterArgument(objectRepository, group);
			if(obj instanceof String){
				String argValue = (String) obj;
				ArgumentDescriptor argumentDescriptor = execDescriptor.descriptor.arguments.get(argPos);
				if(argumentDescriptor != null && argumentDescriptor.typeEnum != null){
					switch (argumentDescriptor.typeEnum) {
					case xml:
						Class<?> xmlClazz = Class.forName(argumentDescriptor.name);
						JAXBContext jaxbContext = JAXBContext.newInstance(xmlClazz);
						Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
						InputStream stream = new ByteArrayInputStream(argValue.getBytes(StandardCharsets.UTF_8));
						args[i] = jaxbUnmarshaller.unmarshal(stream);
						objectRepository.getUserVariables().put(group, args[i]);
						break;
					case json:
						Class<?> jsonClazz = Class.forName(argumentDescriptor.name);
						args[i] = new Gson().fromJson(argValue, jsonClazz);
						objectRepository.getUserVariables().put(group, args[i]);
						break;
					default:
						args[i] = argValue;
						break;
					}
				}else{
					args[i] = argValue;
				}
			}else{
				args[i] = obj;
			}
			if(group.startsWith("$")){
				argPos++;
			}
		}
		return args;
	}

	protected String updateCommandWithVarValues(String inCommand, ActionCommandDescriptor execDescriptor) {
		Matcher matcher = execDescriptor.matcher;
		matcher.matches();
		String outCommand = inCommand;
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
	 * find the method in the action adapter class
	 * matching the command
	 * 
	 * @param command
	 * @param Action Adapter Class
	 * @return
	 */
	public ActionCommandDescriptor findMethodInClass(final String command,
			final Class<?> actionAdapterClass) {
		ActionCommandDescriptor actionCommandWrapper = null;
		Method[] methods = actionAdapterClass.getMethods();
		for (Method method : methods) {
			Annotation[] annotations = method.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation.annotationType().equals(Action.class)) {
					String actionSentence = ((Action) annotation).action();
					CommandArgumentDescriptor commandDescriptor = ArgumentHelper.convertActionSentenceToRegex(actionSentence);
 					Pattern regexPattern = Pattern.compile(commandDescriptor.command);
					Matcher methodMatcher = regexPattern.matcher(command);
					boolean matches = methodMatcher.matches();
					if (matches) {
						Pattern itemRegexPattern = Pattern.compile("\\*(\\$\\w+)\\*");
						Matcher itemMatcher = itemRegexPattern.matcher(command);
						int pos = 0;
						while(itemMatcher.find()){
							String varName = itemMatcher.group(1);
							commandDescriptor.arguments.get(pos).varName = varName;
							if(commandDescriptor.arguments.get(pos).name == null){
								commandDescriptor.arguments.get(pos).name = method.getParameters()[pos].getType().getName();
							}
							pos++;
						}
						actionCommandWrapper = new ActionCommandDescriptor(method, methodMatcher, commandDescriptor);
					}
				}
			}
		}
		if (actionCommandWrapper == null && actionAdapterClass.getSuperclass() != null) {
			return findMethodInClass(command, actionAdapterClass.getSuperclass());
		}
		return actionCommandWrapper;
	}

	private boolean isVariable(Object[] args, int i, String group) {
		return group.startsWith("$") && args[i] != null
				&& 
				!group.contains(Property.DEFAULT_PARAM_SEPARATOR);
	}

	private CommandRequest buildCommandRequest(String command,
			TestLineDescriptor descriptor) {
		final CommandRequest commandRequest;
		switch (descriptor.getTestLineFixtureKind()) {
			case service:
				commandRequest = new CommandRequest.CommandRequestBuilder(null)
						.ofType(ActionAdapterKind.service.name())
						.asCustomCommand(command).build();
				break;
			default:
				commandRequest = new CommandRequest.CommandRequestBuilder(null)
						.asCustomCommand(command).build();
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
		setObjectRepository(injector.getInstance(IActionItemRepository.class));
		this.fixtureApiServices = ActionAdapterCollector.listAvailableServicesByInjection(injector);
	}
	
	public void setObjectRepository(IActionItemRepository repository){
		this.objectRepository = repository;
	}
}
