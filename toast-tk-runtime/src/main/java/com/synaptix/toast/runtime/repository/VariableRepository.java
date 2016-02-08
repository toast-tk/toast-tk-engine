package com.synaptix.toast.runtime.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.synaptix.toast.runtime.IRepository;

public class VariableRepository implements IRepository<Object> {

	private Map<String, Object> userVariables = new HashMap<String, Object>();
	
	@Override
	public Object get(String varName) {
		return userVariables.get(varName);
	}

	@Override
	public Collection<Object> getAll() {
		return userVariables.values();
	}

	@Override
	public void add(String entityName, Object entity) {
		userVariables.put(entityName, entity);
	}

	@Override
	public Map<String, Object> getMap() {
		return userVariables;
	}

	@Override
	public void setMap(Map<String, Object> map) {
		userVariables = map;
	}

	@Override
	public void clear() {
		userVariables.clear();
	}

}
