package com.synaptix.toast.action.interpret.web;

import com.synaptix.toast.core.agent.interpret.WebEventRecord;
import com.synaptix.toast.swing.agent.interpret.MongoRepositoryCacheWrapper;


public class OpenNewPageInterpret extends AbstractInterpretationProvider{

	public OpenNewPageInterpret(MongoRepositoryCacheWrapper mongoRepoManager) {
		super(mongoRepoManager);
	}

	@Override
	public String getSentence(WebEventRecord event) {
		return "Open browser at  *" + event.target + "*";
	}

	@Override
	public String convertToKnowType(String type) {
		return type;
	}
}
