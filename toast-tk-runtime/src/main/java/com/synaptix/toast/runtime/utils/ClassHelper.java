package com.synaptix.toast.runtime.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.synaptix.toast.runtime.action.item.ActionItemValueProvider;

public class ClassHelper {

	public static boolean hasProperty(
		final Class<?> clazz,
		final String property
	) {
		final Method[] methods = clazz.getMethods();
		final String methoProp = property.substring(0, 1).toUpperCase() + property.substring(1);
		for(final Method method : methods) {
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
		final Class<?> klazz,
		final String property
	) {
		final Field[] fields = klazz.getDeclaredFields();
		for(final Field field : fields) {
			if(field.getName().equalsIgnoreCase(property)) {
				return true;
			}
		}
		return false;
	}

	public static Class<?> getPropertyClass(
		final Class<?> clazz,
		final String propertyName
	) {
		final Method[] methods = clazz.getMethods();
		final String methoProp = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		for(final Method method : methods) {
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