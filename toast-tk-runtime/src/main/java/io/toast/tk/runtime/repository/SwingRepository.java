package io.toast.tk.runtime.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.toast.tk.core.runtime.IFeedableSwingPage;
import io.toast.tk.runtime.IRepository;

public class SwingRepository implements IRepository<IFeedableSwingPage> {

	Map<String, IFeedableSwingPage> swingpages = new HashMap<>();
	
	@Override
	public IFeedableSwingPage get(final String entityName) {
		return swingpages.get(entityName);
	}

	@Override
	public Collection<IFeedableSwingPage> getAll() {
		return swingpages.values();
	}

	@Override
	public void add(final String entityName, final IFeedableSwingPage entity) {
		swingpages.put(entityName, entity);
	}

	@Override
	public Map<String, IFeedableSwingPage> getMap() {
		return swingpages;
	}

	@Override
	public void setMap(final Map<String, IFeedableSwingPage> map) {
		this.swingpages = map;
	}

	@Override
	public void clear() {
		this.swingpages.clear();
	}
}