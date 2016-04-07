 package com.synaptix.toast.action.interpret.web;

import com.synaptix.toast.core.agent.interpret.WebEventRecord;
import com.synaptix.toast.swing.agent.interpret.MongoRepositoryCacheWrapper;


public class SelectInterpret extends AbstractInterpretationProvider{

	public SelectInterpret(MongoRepositoryCacheWrapper mongoRepoManager) {
		super(mongoRepoManager);
	}

	@Override
	public String getSentence(
		WebEventRecord event) {
		return "Select *" + event.getValue() + "* in *" + getLabel(event) +"*";
	}

	@Override
	public String convertToKnowType(String type) {
		return type;
	}
}
