package com.synaptix.toast.dao.domain;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.reflect.Reflection;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.dao.domain.impl.test.block.TestPage;

public class DaoBeanFactory {

	// FIXME : add hashmap for implementations instead of hardcoded ClassForName
	private static final Logger LOG = LogManager.getLogger(DaoBeanFactory.class);
	private static final DaoBeanFactory INSTANCE = new DaoBeanFactory();
	
	public static final DaoBeanFactory getInstance(){
		return INSTANCE;
	}
	
	private DaoBeanFactory(){
	}

	public <E> E getBean(Class<E> clazz) {
		try {
			if(!clazz.getName().equals(ITestPage.class.getName())){
				throw new IllegalAccessException("Proxy supported for ITestPage only !");
			}
			final Object implClassInst = TestPage.class.newInstance();
			E proxy = Reflection.newProxy(clazz, new InvocationHandler() {
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					return method.invoke(implClassInst, args);
				}
			});
			return proxy;
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

}
