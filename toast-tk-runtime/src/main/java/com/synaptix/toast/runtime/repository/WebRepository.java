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
		return null;
	}

	@Override
	public Collection<IFeedableWebPage> getAll() {
		return null;
	}

	@Override
	public void add(String entityName, IFeedableWebPage entity) {
		
	}

	@Override
	public Map<String, IFeedableWebPage> getMap() {
		return null;
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
