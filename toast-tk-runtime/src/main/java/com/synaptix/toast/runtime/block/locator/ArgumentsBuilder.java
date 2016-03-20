package com.synaptix.toast.runtime.block.locator;

import javax.script.ScriptException;

import com.google.inject.Injector;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.action.item.ActionItemValueProvider;
import com.synaptix.toast.runtime.action.item.IValueHandler;
import com.synaptix.toast.runtime.bean.ActionCommandDescriptor;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;
import com.synaptix.toast.runtime.utils.ArgumentHelper;

public class ArgumentsBuilder {

	private final ActionCommandDescriptor execDescriptor;
	
	private final ActionItemValueProvider actionItemValueProvider;
	
	private final Injector injector;
	
	private final IActionItemRepository objectRepository;
	
	public ArgumentsBuilder(
		final ActionCommandDescriptor execDescriptor,
		final ActionItemValueProvider actionItemValueProvider,
		final Injector injector,
		final IActionItemRepository objectRepository
	) {
		this.execDescriptor = execDescriptor;
		this.actionItemValueProvider = actionItemValueProvider;
		this.injector = injector;
		this.objectRepository = objectRepository;
	}
	
	public Object[] buildArgumentList() throws Exception {
		System.out.println("Exec descriptor: " + execDescriptor);
		final int groupCount = execDescriptor.matcher.groupCount();
		final Object[] args = new Object[groupCount];
		for(int index = 0; index < groupCount; ++index) {
			final String group = execDescriptor.matcher.group(index + 1);
			final Object obj = ArgumentHelper.buildActionAdapterArgument(objectRepository, group);
			if(obj instanceof String) {
				final String argValue = (String) obj;
				final ArgumentDescriptor argumentDescriptor = execDescriptor.descriptor.arguments.get(index);
				final int argIndex = execDescriptor.isMappedMethod() ? argumentDescriptor.index : index;
				final IValueHandler valueHandlder = actionItemValueProvider.get(argumentDescriptor, injector);
				final Object argument = valueHandlder == null ? group : valueHandlder.handle(group, argValue);
				checkIfArgumentIsNull(argValue, argument);
				args[argIndex] = argument;
			} 
			else {
				args[index] = obj;
			}
		}
		return args;
	}

	private static void checkIfArgumentIsNull(
		final String argValue,
		final Object argument
	) throws ScriptException {
		if(argument == null) {
			throw new ScriptException("Element " + argValue + " was not defined");
		}
	}
}