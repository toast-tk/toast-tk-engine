package io.toast.tk.action.interpret.web;

import io.toast.tk.core.agent.interpret.WebEventRecord;
import io.toast.tk.dao.domain.impl.repository.ProjectImpl;
import io.toast.tk.swing.agent.interpret.MongoRepositoryCacheWrapper;


public class OpenNewPageInterpret extends AbstractInterpretationProvider{

	public OpenNewPageInterpret(MongoRepositoryCacheWrapper mongoRepoManager) {
		super(mongoRepoManager);
	}

	@Override
	public String getSentence(WebEventRecord event, ProjectImpl project) {
		return "Open browser at *" + event.getTarget() + "*";
	}

	@Override
	public String convertToKnowType(String type) {
		return type;
	}
}
