package io.toast.tk.runtime.block;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import io.toast.tk.adapter.cache.ToastCache;
import io.toast.tk.core.annotation.Action;
import io.toast.tk.core.annotation.ActionAdapter;
import io.toast.tk.core.event.TestProgressMessage;
import io.toast.tk.core.runtime.ErrorResultReceivedException;
import io.toast.tk.dao.core.report.FailureResult;
import io.toast.tk.dao.core.report.TestResult;
import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.dao.domain.impl.test.block.TestBlock;
import io.toast.tk.dao.domain.impl.test.block.line.TestLine;
import io.toast.tk.runtime.IActionItemRepository;
import io.toast.tk.runtime.action.item.ActionItemRegexHolder;
import io.toast.tk.runtime.action.item.ActionItemValueProvider;
import io.toast.tk.runtime.bean.ActionCommandDescriptor;
import io.toast.tk.runtime.bean.ArgumentDescriptor;
import io.toast.tk.runtime.bean.CommandArgumentDescriptor;
import io.toast.tk.runtime.block.locator.ActionAdaptaterLocator;
import io.toast.tk.runtime.block.locator.ActionAdaptaterLocators;
import io.toast.tk.runtime.block.locator.ArgumentsBuilder;
import io.toast.tk.runtime.block.locator.NoActionAdapterFound;
import io.toast.tk.runtime.constant.Property;
import io.toast.tk.runtime.result.IResultHandler;
import io.toast.tk.runtime.result.ResultProvider;
import io.toast.tk.runtime.utils.ArgumentHelper;

public class TestBlockRunner implements IBlockRunner<TestBlock> {

	private static final Logger LOG = LogManager.getLogger(TestBlockRunner.class);

	private static final Pattern REGEX_PATTERN = Pattern.compile("\\$(\\d)");

	@Inject
	private IActionItemRepository objectRepository;

	@Inject
	private ActionItemValueProvider actionItemValueProvider;

	@Inject
	private ResultProvider resultProvider;

	@Inject
	private ActionAdaptaterLocators actionAdaptaterLocators;

	private EventBus eventBus;

	@Override
	public void run(final TestBlock block) {
		block.getBlockLines().forEach(line -> invokeTestAndAddResult(block, line));
	}

	private void invokeTestAndAddResult(
		final TestBlock block, 
		final TestLine line
	) {
		final long startTime = System.currentTimeMillis();
		ActionAdaptaterLocator actionCommandDescriptor;
		
		ITestResult result;
		try {
			actionCommandDescriptor = actionAdaptaterLocators.getActionCommandDescriptor(block, line);
			result = invokeActionAdapterAction(actionCommandDescriptor);
			line.setExcutionTime(System.currentTimeMillis() - startTime);
			updateFatal(result, actionCommandDescriptor);
			line.setTestResult((TestResult) result);
		} catch (NoActionAdapterFound e) {
			result = new FailureResult("No Action Adapter found !");
		}
		if(Objects.nonNull(eventBus)){
			eventBus.post(new TestProgressMessage(result));
		}
		if (result.isFatal()) {
			LOG.error("Test execution stopped, due to fail fatal error: {} - Failed !", line);
			throw new FatalExcecutionError();
		}
	}

	/**
	 * Invoke the method matching the test line descriptor
	 */
	protected ITestResult invokeActionAdapterAction(final ActionAdaptaterLocator actionAdaptaterLocator) throws NoActionAdapterFound {
		if (!hasFoundActionAdapter(actionAdaptaterLocator)) {
			throw new NoActionAdapterFound(actionAdaptaterLocator.getTestLineDescriptor().testLine.getTest());
		}
		return doLocalActionCall(actionAdaptaterLocator);
	}

	private static boolean hasFoundActionAdapter(final ActionAdaptaterLocator actionAdaptaterLocator) {
		return actionAdaptaterLocator.getActionAdaptaterClass() != null;
	}

	private static void updateFatal(
			final ITestResult result,
			final ActionAdaptaterLocator actionAdaptaterLocator
	) {
		if (actionAdaptaterLocator.getTestLineDescriptor().isFailFatalCommand() && !result.isSuccess()) {
			result.setIsFatal(true);
		}
	}

	private ITestResult doLocalActionCall(final ActionAdaptaterLocator actionAdaptaterLocator) {
		ITestResult result;
		try {
			Method actionMethod = actionAdaptaterLocator.getActionCommandDescriptor().method;
			Class<?> returnType = actionMethod.getReturnType();
			Object output = actionMethod.invoke(actionAdaptaterLocator.getInstance(), 
												buildArgumentList(actionAdaptaterLocator.getActionCommandDescriptor()));
			String expected = actionAdaptaterLocator.getTestLineDescriptor().testLine.getExpected();
			if (ITestResult.class.isAssignableFrom(returnType)) {
				setContextualTestSentence(actionAdaptaterLocator, (ITestResult) output);
				result = (ITestResult) output;
			} else {
				IResultHandler handler = resultProvider.getHandler(returnType);
				if (handler == null) {
					return new FailureResult("No handler found for result type " + returnType.toString());
				}
				result = handler.result(output, expected);
			}
		} catch (final Exception e) {
			result = handleInvocationError(e);
		} catch (final AssertionError er) {
			result = handleInvocationError(er);
		}
		setContextualTestSentence(actionAdaptaterLocator, result);
		return result;
	}

	private void setContextualTestSentence(
			final ActionAdaptaterLocator actionAdaptaterLocator,
			final ITestResult result
	) {
		if (result != null) {
			result.setContextualTestSentence(updateCommandWithVarValues(actionAdaptaterLocator));
		}
	}

	private static TestResult handleInvocationError(final Exception e) {
		LOG.error(e.getMessage(), e);
		if (e instanceof ErrorResultReceivedException) {
			return ((ErrorResultReceivedException) e).getResult();
		}
		return new FailureResult(ExceptionUtils.getRootCauseMessage(e));
	}

	private static TestResult handleInvocationError(final AssertionError er) {
		LOG.error(er.getMessage(), er);
		return new FailureResult(ExceptionUtils.getRootCauseMessage(er));
	}

	protected Object[] buildArgumentList(
			final ActionCommandDescriptor execDescriptor
	) throws Exception {
		final ArgumentsBuilder argumentsBuilder = new ArgumentsBuilder(execDescriptor, actionItemValueProvider, objectRepository);
		return argumentsBuilder.buildArgumentList();
	}

	private String updateCommandWithVarValues(final ActionAdaptaterLocator actionAdaptaterLocator) {
		final Matcher matcher = actionAdaptaterLocator.getActionCommandDescriptor().matcher;
		matcher.matches();
		String outCommand = actionAdaptaterLocator.getTestLineDescriptor().getActionImpl();
		final int groupCount = matcher.groupCount();
		final Object[] args = new Object[groupCount];
		for (int i = 0; i < groupCount; ++i) {
			final String group = matcher.group(i + 1);
			final Object argument = ArgumentHelper.buildActionAdapterArgument(objectRepository, group);
			args[i] = argument;
			if (isVariable(args, i, group)) {
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
		ToastCache cache = ToastCache.getInstance();
		if (actionAdapterClass != Object.class) {
			final List<Method> actionMethods = cache.getActionMethodsByClass(actionAdapterClass);
			final ActionAdapter adapter = actionAdapterClass.getAnnotation(ActionAdapter.class);
			for (final Method actionMethod : actionMethods) {
				final Action mainAction = actionMethod.getAnnotation(Action.class);
				ActionCommandDescriptor foundMethod = matchMethod(actionImpl, mainAction.action(), actionMethod);
				if (foundMethod != null) {
					return foundMethod;
				} else if (adapter != null && hasMapping(mainAction, adapter)) {
					foundMethod = matchAgainstActionIdMapping(actionImpl, adapter.name(), actionMethod, mainAction);
					if (foundMethod != null) {
						return foundMethod;
					}
				}
			}
			if (actionAdapterClass.getSuperclass() != null) {
				return findMatchingAction(actionImpl, actionAdapterClass.getSuperclass());
			}
		}
		return null;
	}

	private static boolean hasMapping(
			final Action mainAction,
			final ActionAdapter adapter
	) {
		return adapter != null && hasId(mainAction) &&
				ActionSentenceMappingProvider.hasMappingForAction(adapter.name(), mainAction.id());
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
		if (foundMethod != null) {
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
		while (matcher.find()) {
			indexes.add(Integer.valueOf(matcher.group(1)));
		}
		for (final ArgumentDescriptor aDescriptor : foundMethod.descriptor.arguments) {
			aDescriptor.index = indexes.get(pos) - 1;
			++pos;
		}
	}

	private class ActionIndex {

		String item;

		@SuppressWarnings("unused")
		Integer start;

		@SuppressWarnings("unused")
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

	private String buildSustituteAction(
			final Action mainAction,
			String mapping
	) {
		final Matcher matcher = ActionItemRegexHolder.getFullMetaPattern().matcher(mainAction.action());
		final List<ActionIndex> actionItems = new ArrayList<>();
		while (matcher.find()) {
			actionItems.add(new ActionIndex(matcher.group(0), matcher.start(), matcher.end()));
		}
		for (final ActionIndex actionItem : actionItems) {
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
		if (methodMatcher.matches()) {
			final Pattern varRegexPattern = ActionItemRegexHolder.getVarPattern();
			final Matcher varMatcher = varRegexPattern.matcher(actionImpl);
			int pos = 0;
			while (varMatcher.find()) {
				final ArgumentDescriptor argumentDescriptor = commandDescriptor.arguments.get(pos);
				argumentDescriptor.varName = varMatcher.group(1);
				if (argumentDescriptor.name == null) {
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

	private static boolean isVariable(final String group) {
		return group.startsWith("$");
	}

	private static boolean isVariable(Object[] args, int i, String group) {
		return isVariable(group) && args[i] != null && !group.contains(Property.DEFAULT_PARAM_SEPARATOR);
	}

	@Override
	public void setRepository(IActionItemRepository repository) {
		// TODO Auto-generated method stub
		
	}
}

