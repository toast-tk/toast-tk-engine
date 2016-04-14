package com.synaptix.toast.action.interpret.web;

import java.util.List;

import com.synaptix.toast.core.agent.interpret.WebEventRecord;
import com.synaptix.toast.dao.domain.impl.repository.ElementImpl;

public interface IActionInterpret {
	
	String getSentence(WebEventRecord eventRecord);

	void clearElements();

	List<ElementImpl> getElements();
}
