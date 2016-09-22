package io.toast.tk.action.interpret.web;

import java.util.List;

import io.toast.tk.core.agent.interpret.WebEventRecord;
import io.toast.tk.dao.domain.impl.repository.ElementImpl;
import io.toast.tk.dao.domain.impl.repository.ProjectImpl;
import io.toast.tk.dao.domain.impl.repository.RepositoryImpl;

public interface IActionInterpret {
	
	String getSentence(WebEventRecord eventRecord, ProjectImpl project);

	void clearElements();

	List<ElementImpl> getElements();

	RepositoryImpl getRepository();
}
