package com.synaptix.toast.runtime.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.runtime.IRepository;

public class WebContainerRepository implements IRepository<IFeedableWebPage> {

	Map<String, IFeedableWebPage> pages = new HashMap<>();
	
	@Override
	public IFeedableWebPage get(final String entityName) {
		return pages.get(entityName);
	}

	@Override
	public Collection<IFeedableWebPage> getAll() {
		return pages.values();
	}

	@Override
	public void add(
		final String entityName, 
		final IFeedableWebPage entity
	) {
		pages.put(entityName, entity);
	}

	@Override
	public Map<String, IFeedableWebPage> getMap() {
		return pages;
	}

	@Override
	public void setMap(final Map<String, IFeedableWebPage> pages) {
		this.pages = pages;
	}

	@Override
	public void clear() {
		pages.clear();		
	}
}