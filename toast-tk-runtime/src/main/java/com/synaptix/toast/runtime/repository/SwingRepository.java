package com.synaptix.toast.runtime.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.runtime.IRepository;

public class SwingRepository implements IRepository<IFeedableSwingPage> {

	Map<String, IFeedableSwingPage> swingpages = new HashMap<String, IFeedableSwingPage>();
	
	@Override
	public IFeedableSwingPage get(String entityName) {
		return swingpages.get(entityName);
	}

	@Override
	public Collection<IFeedableSwingPage> getAll() {
		return swingpages.values();
	}

	@Override
	public void add(String entityName, IFeedableSwingPage entity) {
		swingpages.put(entityName, entity);
	}

	@Override
	public Map<String, IFeedableSwingPage> getMap() {
		return swingpages;
	}

	@Override
	public void setMap(Map<String, IFeedableSwingPage> map) {
		this.swingpages = map;
	}

	@Override
	public void clear() {
		this.swingpages.clear();
	}

}
