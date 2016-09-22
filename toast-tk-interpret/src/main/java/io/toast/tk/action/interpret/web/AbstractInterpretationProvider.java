package io.toast.tk.action.interpret.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.util.Strings;

import io.toast.tk.core.agent.interpret.WebEventRecord;
import io.toast.tk.dao.domain.impl.repository.ElementImpl;
import io.toast.tk.dao.domain.impl.repository.ProjectImpl;
import io.toast.tk.dao.domain.impl.repository.RepositoryImpl;
import io.toast.tk.swing.agent.interpret.MongoRepositoryCacheWrapper;

public abstract class AbstractInterpretationProvider implements IActionInterpret {

	private MongoRepositoryCacheWrapper mongoRepoManager;
	private List<ElementImpl> elements;
	private RepositoryImpl container;

	public AbstractInterpretationProvider(MongoRepositoryCacheWrapper mongoRepoManager){
		this.mongoRepoManager = mongoRepoManager;
		this.elements = new ArrayList<>();
	}
	
	public String getLabel(WebEventRecord eventRecord, ProjectImpl project) {
		this.container = mongoRepoManager.findContainer(eventRecord.getParent(), "web page", project);
		ElementImpl element = mongoRepoManager.findElement(container, eventRecord); //updates also the container
		elements.add(element);
		return container.getName() + "." + getElementLabel(element);
	}
	
	@Override
	public RepositoryImpl getRepository(){
		return this.container;
	}
	
	@Override
	public List<ElementImpl> getElements(){
		return new ArrayList<>(elements);
	}
	
	@Override
	public void clearElements(){
		elements.clear();
	}
	
	public String getElementLabel(ElementImpl element){
		return Strings.isEmpty(element.getName()) ? element.locator : element.getName();
	}

	public abstract String convertToKnowType(String type);

	
}
