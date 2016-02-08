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

	private static final Log LOG = LogFactory.getLog(ActionItemRepository.class);

	private final SwingRepository swingRepo;

	private final WebContainerRepository webRepo;
	
	
	private final WebComponentRepository webComponentRepo;
	

	private VariableRepository varRepo;
	
	@Inject
	public ActionItemRepository(SwingRepository swingRepo, WebContainerRepository webRepo, VariableRepository varRepo, WebComponentRepository webComponentRepo){
		this.swingRepo = swingRepo;
		this.webRepo = webRepo;
		this.webComponentRepo = webComponentRepo;
		this.varRepo = varRepo;
	}

	@Override
	public Map<String, Object> getUserVariables()
	{
		return this.varRepo.getMap();
	}

	@Override
	public void setUserVariables(
		Map<String, Object> userVariables) {
		this.varRepo.setMap(userVariables);
	}

	@Override
	public void addSwingPage(String pageName) {
		this.swingRepo.add(pageName, new DefaultSwingPage());
	}

	@Override
	public IFeedableSwingPage getSwingPage(String pageName) {
		return this.swingRepo.get(pageName);
	}

	@Override
	public IFeedableWebPage getWebPage(String entityName) {
		return this.webRepo.get(entityName);
	}

	@Override
	public Collection<IFeedableSwingPage> getSwingPages() {
		return this.swingRepo.getAll();
	}

	@Override
	public Collection<IFeedableWebPage> getWebPages() {
		return this.webRepo.getAll();
	}

	@Override
	public void addWebPage(String fixtureName, IFeedableWebPage webPage) {
		this.webRepo.add(fixtureName, webPage);		
	}
	
	@Override
	public void clear() {
		this.swingRepo.clear();
		this.webRepo.clear();
		this.varRepo.clear();
		this.webComponentRepo.clear();
	}

	@Override
	public Map<String, IWebAutoElement<?>> getWebComponents() {
		return this.webComponentRepo.getMap();
	}

	@Override
	public void setWebComponents(Map<String, IWebAutoElement<?>> webComponents) {
		this.webComponentRepo.setMap(webComponents);
	}

}