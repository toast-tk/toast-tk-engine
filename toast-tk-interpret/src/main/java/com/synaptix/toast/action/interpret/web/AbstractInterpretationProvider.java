package com.synaptix.toast.action.interpret.web;

import com.synaptix.toast.core.agent.interpret.WebEventRecord;
import com.synaptix.toast.dao.domain.impl.repository.RepositoryImpl;
import com.synaptix.toast.swing.agent.interpret.MongoRepositoryCacheWrapper;

public abstract class AbstractInterpretationProvider implements IActionInterpret {

	private MongoRepositoryCacheWrapper mongoRepoManager;

	public AbstractInterpretationProvider(MongoRepositoryCacheWrapper mongoRepoManager){
		this.mongoRepoManager = mongoRepoManager;
	}
	
	public String getLabel(WebEventRecord eventRecord) {
		RepositoryImpl container = mongoRepoManager.findContainer(eventRecord.parent, "web page");
		String label = mongoRepoManager.find(container,
				eventRecord);
		return container.name +"."+label;
	}

	public abstract String convertToKnowType(String type);

	
}
