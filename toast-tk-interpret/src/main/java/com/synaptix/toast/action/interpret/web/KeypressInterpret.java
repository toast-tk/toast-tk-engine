package com.synaptix.toast.action.interpret.web;

import com.synaptix.toast.core.agent.interpret.WebEventRecord;
import com.synaptix.toast.dao.domain.impl.repository.RepositoryImpl;
import com.synaptix.toast.swing.agent.interpret.MongoRepositoryCacheWrapper;


public class KeypressInterpret extends AbstractInterpretationProvider{

	public KeypressInterpret(MongoRepositoryCacheWrapper mongoRepoManager) {
		super(mongoRepoManager);
	}

	@Override
	public String getSentence(
		WebEventRecord event) {
		return "Type *"+event.getValue()+"* in *"+ getLabel(event) +"*";
	}

	@Override
	public String convertToKnowType(String type) {
		return "input";
	}
}
