package com.synaptix.toast.runtime.result;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.synaptix.toast.runtime.IActionItemRepository;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class ResultProvider {

    @Inject
    private IActionItemRepository objectRepository;

    private Map<Class<?>, IResultHandler> map;

    public ResultProvider() {

    }

    public IResultHandler getHandler(Class<?> clazz) {
        if (map == null) {
            map = new HashMap<>();
            map.put(String.class, new StringResultHandler(objectRepository));
            map.put(Void.TYPE, new VoidResultHandler(objectRepository));
            map.put(Integer.class, new IntegerResultHandler(objectRepository));
        }
        return map.get(clazz);
    }

}