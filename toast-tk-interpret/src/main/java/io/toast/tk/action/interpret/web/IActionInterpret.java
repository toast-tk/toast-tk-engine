package io.toast.tk.action.interpret.web;

import java.util.List;

import io.toast.tk.core.agent.interpret.WebEventRecord;
import io.toast.tk.dao.domain.impl.repository.ElementImpl;

public interface IActionInterpret {
	
	String getSentence(WebEventRecord eventRecord);

	void clearElements();

	List<ElementImpl> getElements();
}
