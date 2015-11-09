package com.synaptix.toast.runtime.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;

public class ClassHelper {

	private Object straightGetProperty(
		Object object,
		String property) {
		try {
			return BeanUtils.getProperty(object, property);
		}
		catch(IllegalAccessException e) {
			e.printStackTrace();
		}
		catch(InvocationTargetException e) {
			e.printStackTrace();
		}
		catch(NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean hasProperty(
		Class<?> clazz,
		String property) {
		Method[] methods = clazz.getMethods();
		String methoProp = property.substring(0, 1).toUpperCase() + property.substring(1);
		for(Method method : methods) {
			if(method.getName().equals("get" + methoProp)) {
				return true;
			}
			else if(method.getName().equals("set" + methoProp)) {
				return true;
			}
			else if(method.getName().equals("has" + methoProp)) {
				return true;
			}
			else if(method.getName().equals("is" + methoProp)) {
				return true;
			}
			else if(method.getName().equals(property)) {
				return true;
			}
		}
		return inspect(clazz, property);
	}

	static boolean inspect(
		Class<?> klazz,
		String property) {
		Field[] fields = klazz.getDeclaredFields();
		for(Field field : fields) {
			if(field.getName().equalsIgnoreCase(property)) {
				return true;
			}
		}
		return false;
	}

	public static Class<?> getPropertyClass(
		Class<?> clazz,
		String propertyName) {
		Method[] methods = clazz.getMethods();
		String methoProp = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		for(Method method : methods) {
			if(method.getName().equals("get" + methoProp)) {
				return method.getReturnType();
			}
			else if(method.getName().equals("has" + methoProp)) {
				return method.getReturnType();
			}
			else if(method.getName().equals("is" + methoProp)) {
				return method.getReturnType();
			}
			else if(method.getName().equals(propertyName)) {
				return method.getReturnType();
			}
		}
		return null;
	}
}
