package com.synaptix.toast.runtime;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.synaptix.toast.adapter.swing.component.DefaultSwingPage;
import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.runtime.repository.SwingRepository;
import com.synaptix.toast.runtime.repository.VariableRepository;
import com.synaptix.toast.runtime.repository.WebRepository;

public class ActionItemRepository implements IActionItemRepository {

	private static final Log LOG = LogFactory.getLog(ActionItemRepository.class);

	private final SwingRepository swingRepo;

	private final WebRepository webRepo;

	private VariableRepository varRepo;
	
	@Inject
	public ActionItemRepository(SwingRepository swingRepo, WebRepository webRepo, VariableRepository varRepo){
		this.swingRepo = swingRepo;
		this.webRepo = webRepo;
		this.varRepo = varRepo;
	}

	@Override
	public Map<String, Object> getUserVariables() {
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
	}

}