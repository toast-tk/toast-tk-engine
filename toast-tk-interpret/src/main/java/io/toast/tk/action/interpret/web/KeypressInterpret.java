package io.toast.tk.action.interpret.web;

import io.toast.tk.core.agent.interpret.WebEventRecord;
import io.toast.tk.dao.domain.impl.repository.ProjectImpl;
import io.toast.tk.swing.agent.interpret.MongoRepositoryCacheWrapper;


public class KeypressInterpret extends AbstractInterpretationProvider{

	public KeypressInterpret(MongoRepositoryCacheWrapper mongoRepoManager) {
		super(mongoRepoManager);
	}

	@Override
	public String getSentence(
		WebEventRecord event, ProjectImpl project) {
		return "Type *"+event.getValue()+"* in *"+ getLabel(event, project) +"*";
	}

	@Override
	public String convertToKnowType(String type) {
		return "input";
	}
}
