package com.synaptix.toast.runtime.result;

import com.synaptix.toast.runtime.IActionItemRepository;

import java.util.HashMap;
import java.util.Map;

public class ResultProvider {

    private Map<Class<?>, IResultHandler> map;

    public ResultProvider(IActionItemRepository objectRepository) {
        map = new HashMap<>();
        map.put(String.class, new StringResultHandler(objectRepository));
        map.put(Void.TYPE, new VoidResultHandler(objectRepository));
        map.put(Integer.class, new IntegerResultHandler(objectRepository));
    }

    public IResultHandler getHandler(Class<?> clazz) {
        return map.get(clazz);
    }

}