package io.toast.tk.dao.service.dao.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityCollectionManager {

	private static final Logger LOG = LogManager.getLogger(EntityCollectionManager.class);
	
	Map<Class<?>, String> mongoEntityMap = new HashMap<>();

	public void register(
		final String value,
		final Class<?> c
	) {
		try {
			final Class<?> interfaceClass = Class.forName("I" + c.getSimpleName());
			mongoEntityMap.put(interfaceClass, value);
		}
		catch(final Exception e) {
			LOG.error(e.getMessage(), e);
		}
		mongoEntityMap.put(c, value);
	}

	public String getCollection(final Class<?> clazz) {
		return mongoEntityMap.get(clazz);
	}
}