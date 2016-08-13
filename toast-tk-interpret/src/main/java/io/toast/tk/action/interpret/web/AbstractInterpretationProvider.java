package io.toast.tk.action.interpret.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.util.Strings;

import io.toast.tk.core.agent.interpret.WebEventRecord;
import io.toast.tk.dao.domain.impl.repository.ElementImpl;
import io.toast.tk.dao.domain.impl.repository.RepositoryImpl;
import io.toast.tk.swing.agent.interpret.MongoRepositoryCacheWrapper;

public abstract class AbstractInterpretationProvider implements IActionInterpret {

	private MongoRepositoryCacheWrapper mongoRepoManager;
	private List<ElementImpl> elements;

	public AbstractInterpretationProvider(MongoRepositoryCacheWrapper mongoRepoManager){
		this.mongoRepoManager = mongoRepoManager;
		this.elements = new ArrayList<>();
	}
	
	public String getLabel(WebEventRecord eventRecord) {
		RepositoryImpl container = mongoRepoManager.findContainer(eventRecord.getParent(), "web page");
		ElementImpl element = mongoRepoManager.findElement(container, eventRecord);
		elements.add(element);
		return container.getName() + "." + getElementLabel(element);
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
