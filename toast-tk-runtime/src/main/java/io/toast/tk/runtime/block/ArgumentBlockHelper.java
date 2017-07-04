package io.toast.tk.runtime.block;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ArgumentBlockHelper {

	private static final Logger LOG = LogManager.getLogger(ArgumentBlockHelper.class);

	public static boolean isArgumentFailureMessage(Parameter[] params, 
			Object[] inputArgList) {
		String inputArg = "";
		try {
			for(Object input : inputArgList) {
				inputArg = writeMessage(inputArg, input);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
		String arg = "";
		for(Parameter param : params) {
			arg = writeMessage(arg, param.getParameterizedType());
		}

		String[] inputArgs = inputArg.split(System.lineSeparator());
		String[] args = arg.split(System.lineSeparator());
		
		if(inputArgs.length != args.length) {
			return true;
		}
		
		for(int i = 0; i < inputArgs.length; i ++) {
			String inputTemp = inputArgs[i];
			String argTemp = args[i];
			if(!inputTemp.equals(argTemp)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static String getArgumentFailureMessage(Parameter[] params, 
			Object[] inputArgList) {
		String res = "Your inputs arguments are :" + System.lineSeparator();
		try {
			String inputArg = "";
			for(Object input : inputArgList) {
				inputArg = writeMessage(inputArg, input);
			}
			res += inputArg;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
		res += System.lineSeparator() + "Instead of :" + System.lineSeparator();

		String args = "";
		for(Parameter param : params) {
			args = writeMessage(args, param.getParameterizedType());
		}
		res += args;
		
		return res;
	}
	
	@SuppressWarnings("rawtypes")
	private static String writeMessage(String args, Object input) {
		String className = input.getClass().getName().trim();
		if(input instanceof List) {
			if(((List) input).size() != 0) {
				String name = ((List) input).get(0).getClass().getName().trim();
				name = getClassName(name);
				className = "List of " + name;
			}
		}
		return writeMessage(args, className);
	}
	private static String writeMessage(String args, Type inputType) {
		String className = inputType.getTypeName().trim();
		if(className.startsWith("java.util.List")) {
			className = className.substring("java.util.List<".length(), className.length() - ">".length());
			className = "List of " + getClassName(className);
		} else if(className.startsWith("java.util.Map")) {
			className = className.substring("java.util.Map<".length(), className.length() - ">".length());
			className = "Map of " + getMapClassName(className);
		}
		return writeMessage(args, className);
	}
	private static String writeMessage(String args, String className) {
		String name = "- " + getClassName(className);
		String res = args.equals("") ? name : args + System.lineSeparator() + name;
		return res;
	}
	private static String getClassName(String className) {
		String name = className.substring(className.lastIndexOf(".") + 1).trim();
		name = "HashMap".equals(name) ? "Map" : name;
		return name;
	}
	private static String getMapClassName(String className) {
		String[] classes = className.split(",");
		return getClassName(classes[0]) + " and " + getClassName(classes[1]);
	}
	
}
