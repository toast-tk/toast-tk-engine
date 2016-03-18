package com.synaptix.toast.runtime;

import java.util.Collection;
import java.util.Map;

public interface IRepository<E> {

	E get(final String entityName);

	Collection<E> getAll();
	
	void add(final String entityName, final E entity);
	
	Map<String,E> getMap();

	void setMap(final Map<String,E> map);
	
	void clear();
}