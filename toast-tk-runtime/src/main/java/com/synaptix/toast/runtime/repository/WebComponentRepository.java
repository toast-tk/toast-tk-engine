package com.synaptix.toast.runtime.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.core.runtime.IWebAutoElement;
import com.synaptix.toast.runtime.IRepository;

public class WebComponentRepository implements IRepository<IWebAutoElement<?>> {

	Map<String, IWebAutoElement<?>> components = new HashMap<>();
	
	@Override
	public IWebAutoElement<?> get(String entityName) {
		return components.get(entityName);
	}

	@Override
	public Collection<IWebAutoElement<?>> getAll() {
		return components.values();
	}

	@Override
	public void add(String entityName, IWebAutoElement<?> entity) {
		components.put(entityName, entity);
	}

	@Override
	public Map<String, IWebAutoElement<?>> getMap() {
		return components;
	}

	@Override
	public void setMap(Map<String, IWebAutoElement<?>> map) {
		this.components = map;
	}

	@Override
	public void clear() {
		components.clear();		
	}

}
