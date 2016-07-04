package io.toast.tk.action.interpret.web;

import io.toast.tk.core.agent.interpret.WebEventRecord;
import io.toast.tk.swing.agent.interpret.MongoRepositoryCacheWrapper;


public class WebClickInterpret extends AbstractInterpretationProvider{

	public WebClickInterpret(MongoRepositoryCacheWrapper mongoRepoManager) {
		super(mongoRepoManager);
	}

	@Override
	public String getSentence(
		WebEventRecord event) {
		return "Click on *" + getLabel(event) + "*";
	}

	@Override
	public String convertToKnowType(String type) {
		return type;
	}
}
