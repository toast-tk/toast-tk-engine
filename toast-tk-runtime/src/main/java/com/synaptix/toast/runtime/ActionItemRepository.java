package com.synaptix.toast.runtime;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.synaptix.toast.adapter.swing.component.DefaultSwingPage;
import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.core.runtime.IWebAutoElement;
import com.synaptix.toast.runtime.repository.SwingRepository;
import com.synaptix.toast.runtime.repository.VariableRepository;
import com.synaptix.toast.runtime.repository.WebComponentRepository;
import com.synaptix.toast.runtime.repository.WebContainerRepository;

public class ActionItemRepository implements IActionItemRepository {

	private final SwingRepository swingRepo;

	private final WebContainerRepository webRepo;
	
	private final WebComponentRepository webComponentRepo;
	
	private VariableRepository varRepo;
	
	@Inject
	public ActionItemRepository(
		final SwingRepository swingRepo, 
		final WebContainerRepository webRepo, 
		final VariableRepository varRepo, 
		final WebComponentRepository webComponentRepo
	) {
		this.swingRepo = swingRepo;
		this.webRepo = webRepo;
		this.webComponentRepo = webComponentRepo;
		this.varRepo = varRepo;
	}

	@Override
	public Map<String, Object> getUserVariables() {
		return varRepo.getMap();
	}

	@Override
	public void setUserVariables(final Map<String, Object> userVariables) {
		varRepo.setMap(userVariables);
	}

	@Override
	public void addSwingPage(final String pageName) {
		swingRepo.add(pageName, new DefaultSwingPage());
	}

	@Override
	public IFeedableSwingPage getSwingPage(final String pageName) {
		return swingRepo.get(pageName);
	}

	@Override
	public IFeedableWebPage getWebPage(final String entityName) {
		return webRepo.get(entityName);
	}

	@Override
	public Collection<IFeedableSwingPage> getSwingPages() {
		return swingRepo.getAll();
	}

	@Override
	public Collection<IFeedableWebPage> getWebPages() {
		return webRepo.getAll();
	}

	@Override
	public void addWebPage(final String fixtureName, final IFeedableWebPage webPage) {
		webRepo.add(fixtureName, webPage);		
	}
	
	@Override
	public void clear() {
		swingRepo.clear();
		webRepo.clear();
		varRepo.clear();
		webComponentRepo.clear();
	}

	@Override
	public Map<String, IWebAutoElement<?>> getWebComponents() {
		return webComponentRepo.getMap();
	}

	@Override
	public void setWebComponents(final Map<String, IWebAutoElement<?>> webComponents) {
		webComponentRepo.setMap(webComponents);
	}
}