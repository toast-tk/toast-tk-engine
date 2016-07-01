package com.synaptix.toast.adapter.cache;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;


public class ToastCacheTest {

    @Test
    public void testInitAdapters() {
        Set<Class<?>> services = ToastCache.getInstance().getServices();
        Assert.assertTrue(services.size() > 0);
        Assert.assertTrue(services.contains(CacheTestAdapter.class));
    }

    @Test
    public void testMethods() {
        Set<Method> actionMethods = ToastCache.getInstance().getActionMethods();
        Assert.assertTrue(actionMethods.size() > 0);
        try {
            Assert.assertTrue(actionMethods.contains(CacheTestAdapter.class.getMethod("cacheAction")));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}