package io.toast.tk.action.interpret.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;

import io.toast.tk.swing.agent.interpret.MongoRepositoryCacheWrapper;

public class InterpretationProvider {

	private MongoRepositoryCacheWrapper mongoRepoManager;


	@Inject
	public InterpretationProvider(MongoRepositoryCacheWrapper mongoRepoManager){
		this.mongoRepoManager = mongoRepoManager;

	}

	public IActionInterpret getSentenceBuilder(String type) {
		return getInterpretFor(type);
	}

	private IActionInterpret getInterpretFor(final String type) {
		String formatedType = type;
		if (formatedType != null && formatedType.contains(":")) {
			formatedType = StringUtils.split(type, ":")[0];
		}
		return get(formatedType);
	}

	private IActionInterpret get(String type) {
		switch (type) {
		case "a":
			return new WebClickInterpret(this.mongoRepoManager);
		case "select":
			return new SelectInterpret(this.mongoRepoManager);
		case "button":
			return new WebClickInterpret(this.mongoRepoManager);
		case "text":
			return new KeypressInterpret(this.mongoRepoManager);
		case "open":
			return new OpenNewPageInterpret(this.mongoRepoManager);
		default:
			return null;
		}
	}
}
