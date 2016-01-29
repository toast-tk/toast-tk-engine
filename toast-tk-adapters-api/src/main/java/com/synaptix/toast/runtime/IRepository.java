package com.synaptix.toast.runtime;

import java.util.Collection;
import java.util.Map;

public interface IRepository<E> {

	public E get(String entityName);

	public Collection<E> getAll();
	
	public void add(String entityName, E entity);
	
	public Map<String,E> getMap();

	public void setMap(Map<String,E> map);
	
	public void clear();

}
