package com.synaptix.toast.dao.domain;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.reflect.Reflection;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.dao.domain.impl.test.block.TestPage;

public class DaoBeanFactory {


	private static final Logger LOG = LogManager.getLogger(DaoBeanFactory.class);
	
	private static final DaoBeanFactory INSTANCE = new DaoBeanFactory();
	
	public static final DaoBeanFactory getInstance(){
		return INSTANCE;
	}
	
	private DaoBeanFactory() {
	}

	public static <E> E getBean(final Class<E> clazz) {
		try {
			checkIfIsTestPage(clazz);
			final Object implClassInst = TestPage.class.newInstance();
			return Reflection.newProxy(clazz, new InvocationHandler() {
				@Override
				public Object invoke(
					final Object proxy, 
					final Method method, 
					final Object[] args
				) throws Throwable {
					return method.invoke(implClassInst, args);
				}
			});
		} 
		catch(final InstantiationException | IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	private static <E> void checkIfIsTestPage(final Class<E> clazz)
			throws IllegalAccessException {
		if(isNotITestPage(clazz)) {
			throw new IllegalAccessException("Proxy supported for ITestPage only !");
		}
	}

	private static <E> boolean isNotITestPage(final Class<E> clazz) {
		return (clazz != ITestPage.class);
	}
}