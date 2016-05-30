package com.synaptix.toast.runtime.block;

import com.google.inject.Injector;
import com.synaptix.toast.adapter.cache.ToastCache;
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
import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;
import com.synaptix.toast.runtime.bean.CommandArgumentDescriptor;
import com.synaptix.toast.runtime.block.locator.ActionAdaptaterLocator;
import com.synaptix.toast.runtime.block.locator.ActionAdaptaterLocators;
import com.synaptix.toast.runtime.block.locator.ArgumentsBuilder;
import com.synaptix.toast.runtime.constant.Property;
import com.synaptix.toast.runtime.result.IResultHandler;
import com.synaptix.toast.runtime.result.ResultProvider;
import com.synaptix.toast.runtime.utils.ArgumentHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestBlockRunner implements IBlockRunner<TestBlock> {

    private static final Logger LOG = LogManager.getLogger(BlockRunnerProvider.class);

    private static final Pattern REGEX_PATTERN = Pattern.compile("\\$(\\d)");

    private IActionItemRepository objectRepository;

    private ActionItemValueProvider actionItemValueProvider;

    private Injector injector;
    private ResultProvider resultProvider;

    @Override
    public void run(final TestBlock block) {
        block.getBlockLines().stream().forEach(line -> invokeTestAndAddResult(block, line));
    }

    private boolean invokeTestAndAddResult(
            final TestBlock block,
            final TestLine line
    ) {
        final long startTime = System.currentTimeMillis();
        final ActionAdaptaterLocator actionCommandDescriptor = ActionAdaptaterLocators.getInstance()
                .getActionCommandDescriptor(block, line, injector);
        final TestResult result = invokeActionAdapterAction(actionCommandDescriptor);
        line.setExcutionTime(System.currentTimeMillis() - startTime);
        finaliseResultKind(line, result);
        line.setTestResult(result);
        if (result.isFatal()) {
            LOG.error("Test execution stopped, due to fail fatal error: {} - Failed !", line);
            return false;
        }
        return true;
    }

    private static void finaliseResultKind(
            final TestLine line,
            final ITestResult result
    ) {
        if (isFailureExpected(line, result) || isExpectedResult(line, result)) {
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
        return result.getMessage() != null && line.getExpected() != null && result.getMessage().equals(line
				.getExpected());
    }

    private boolean hasFoundActionAdapter(Class<?> actionAdapter) {
        return actionAdapter != null;
    }

    /**
     * invoke the method matching the test line descriptor
     */
    protected TestResult invokeActionAdapterAction(final ActionAdaptaterLocator actionAdaptaterLocator) {
        if (hasFoundActionAdapter(actionAdaptaterLocator)) {
            final ITestResult result = doLocalActionCall(actionAdaptaterLocator);
            updateFatal(result, actionAdaptaterLocator);
            return (TestResult) result;
        }
        return new FailureResult("Action Implementation - Not Found");
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
            result.setIsError(false);
            result.setIsFailure(false);
            result.setResultKind(ResultKind.FATAL);
        }
    }

    private ITestResult doLocalActionCall(final ActionAdaptaterLocator actionAdaptaterLocator) {
        TestResult result;
        try {
            Method actionMethod = actionAdaptaterLocator.getActionCommandDescriptor().method;
            Class<?> returnType = actionMethod.getReturnType();
            Object output = actionMethod.invoke(actionAdaptaterLocator.getInstance(), buildArgumentList
					(actionAdaptaterLocator.getActionCommandDescriptor()));

            String expected = actionAdaptaterLocator.getTestLineDescriptor().testLine.getExpected();

            IResultHandler handler = resultProvider.getHandler(returnType);
            return handler.result(output, expected);

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
            final TestResult result
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
        final ArgumentsBuilder argumentsBuilder = new ArgumentsBuilder(execDescriptor, actionItemValueProvider,
				injector, objectRepository);
        return argumentsBuilder.buildArgumentList();
    }

    protected String updateCommandWithVarValues(final ActionAdaptaterLocator actionAdaptaterLocator) {
        final Matcher matcher = actionAdaptaterLocator.getActionCommandDescriptor().matcher;
        matcher.matches();
        String outCommand = actionAdaptaterLocator.getTestLineDescriptor().getActionImpl();
        final int groupCount = matcher.groupCount();
        final Object[] args = new Object[groupCount];
        for (int i = 0; i < groupCount; ++i) {
            final String group = matcher.group(i + 1);
            args[i] = ArgumentHelper.buildActionAdapterArgument(objectRepository, group);
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

    private static boolean isVariable(final String group) {
        return group.startsWith("$");
    }

    private static boolean isVariable(Object[] args, int i, String group) {
        return isVariable(group) && args[i] != null && !group.contains(Property.DEFAULT_PARAM_SEPARATOR);
    }

    @Override
    public void setInjector(final Injector injector) {
        this.injector = injector;
        this.actionItemValueProvider = injector.getInstance(ActionItemValueProvider.class);
        this.objectRepository = injector.getInstance(IActionItemRepository.class);
        this.resultProvider = new ResultProvider(objectRepository);
    }

    public void setObjectRepository(IActionItemRepository repository) {
        this.objectRepository = repository;
    }

    public void setActionItemValueProvider(ActionItemValueProvider repository) {
        this.actionItemValueProvider = repository;
    }
}