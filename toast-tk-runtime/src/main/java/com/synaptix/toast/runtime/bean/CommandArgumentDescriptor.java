package com.synaptix.toast.runtime.bean;

import java.util.List;

public class CommandArgumentDescriptor {

	public final String regex;

	public final List<ArgumentDescriptor> arguments;
	
	public CommandArgumentDescriptor(
		final String regex,
		final List<ArgumentDescriptor> arguments
	) {
		this.regex = regex;
		this.arguments = arguments;
	}
}