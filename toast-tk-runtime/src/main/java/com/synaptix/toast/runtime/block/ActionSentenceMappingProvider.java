package com.synaptix.toast.runtime.block;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class ActionSentenceMappingProvider {

	public static final String MAPPING_FILE = "toast.yml";
	static InputStream resourceAsStream = ActionSentenceMappingProvider.class.getClassLoader().getResourceAsStream(MAPPING_FILE);
	static Yaml yaml = new Yaml();
	@SuppressWarnings("unchecked")
	static Map<String, List<Map<String,String>>> beanMap = (Map<String, List<Map<String,String>>>)yaml.load(resourceAsStream);
	
	private static boolean hasMappingForAdapter(String adapterName){
		return beanMap.containsKey(adapterName);
	}
	
	public static boolean hasMappingForAction(String adapterName, String actionId){
		List<Map<String,String>> list = (List<Map<String,String>>) beanMap.get(adapterName);
		return list == null ? false : list.stream().anyMatch(m -> m.containsKey(actionId));
	}
	
	public static String getMappingForAction(String adapterName, String actionId){
		String output = null;
		if(ActionSentenceMappingProvider.hasMappingForAdapter(adapterName)){
			if(ActionSentenceMappingProvider.hasMappingForAction(adapterName, actionId)){
				List<Map<String,String>> list = (List<Map<String,String>>) beanMap.get(adapterName);
				output = list.stream().filter(m -> m.containsKey(actionId)).findFirst().get().get(actionId);
			}
		}
		return output;
	}

}
