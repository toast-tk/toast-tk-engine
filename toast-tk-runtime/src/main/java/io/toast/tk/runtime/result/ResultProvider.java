package io.toast.tk.runtime.result;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.toast.tk.runtime.IActionItemRepository;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class ResultProvider {

    @Inject
    private IActionItemRepository objectRepository;
    private ObjectResultHandler defaultResultHandler;

    private Map<Class<?>, IResultHandler<?>> map;

    public ResultProvider() {
    	
    }

    public IResultHandler<?> getHandler(Class<?> clazz) {
        if (map == null) {
            map = new HashMap<>();
            map.put(String.class, new StringResultHandler(objectRepository));
            map.put(Void.TYPE, new VoidResultHandler(objectRepository));
            map.put(Integer.class, new IntegerResultHandler(objectRepository));
            map.put(Boolean.class, new BooleanResultHandler(objectRepository));
            this.defaultResultHandler = new ObjectResultHandler(objectRepository);
        }
        return map.getOrDefault(clazz, defaultResultHandler);
    }

}