package io.toast.tk.action.interpret.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;

import io.toast.tk.swing.agent.interpret.MongoRepositoryCacheWrapper;


public class InterpretationProvider {
	
	private MongoRepositoryCacheWrapper mongoRepoManager;

	private Map<String, IActionInterpret> map;
	
	@Inject
	public InterpretationProvider(MongoRepositoryCacheWrapper mongoRepoManager){
		this.mongoRepoManager = mongoRepoManager;
		map = new HashMap<String, IActionInterpret>();
		map.put("a", new WebClickInterpret(this.mongoRepoManager));
		map.put("select", new SelectInterpret(this.mongoRepoManager));
		map.put("button", new WebClickInterpret(this.mongoRepoManager));
		map.put("text", new KeypressInterpret(this.mongoRepoManager));
		map.put("open", new OpenNewPageInterpret(this.mongoRepoManager));
	}

	public IActionInterpret getSentenceBuilder(
		String type) {
		return getInterpretFor(type);
	}

	private IActionInterpret getInterpretFor(
		final String type) {
		String formatedType = type;
		if(formatedType != null && formatedType.contains(":")){
			formatedType = StringUtils.split(type, ":")[0];
		}
		return map.get(formatedType);
	}
}
