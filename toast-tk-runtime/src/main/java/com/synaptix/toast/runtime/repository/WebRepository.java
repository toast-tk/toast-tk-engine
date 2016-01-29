package com.synaptix.toast.runtime.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.runtime.IRepository;

public class WebRepository implements IRepository<IFeedableWebPage> {

	Map<String, IFeedableWebPage> pages = new HashMap<String, IFeedableWebPage>();
	
	@Override
	public IFeedableWebPage get(String entityName) {
		return pages.get(entityName);
	}

	@Override
	public Collection<IFeedableWebPage> getAll() {
		return pages.values();
	}

	@Override
	public void add(String entityName, IFeedableWebPage entity) {
		pages.put(entityName, entity);
	}

	@Override
	public Map<String, IFeedableWebPage> getMap() {
		return pages;
	}

	@Override
	public void setMap(Map<String, IFeedableWebPage> map) {
		this.pages = map;
	}

	@Override
	public void clear() {
		pages.clear();		
	}

}
