package com.synaptix.toast.runtime.block;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.synaptix.toast.adapter.constant.AdaptersConfigProvider;

@SuppressWarnings("unchecked")
public class ActionSentenceMappingProvider {

	static final Map<String, List<Map<String,String>>> beanMap;

	static {
		try(final InputStream resourceAsStream = ActionSentenceMappingProvider.class.getClassLoader().getResourceAsStream("toast.yml");) {
			beanMap = ((Map<String, List<Map<String,String>>>) new Yaml().load(resourceAsStream));
		}
		catch(final IOException e) {
			LogManager.getLogger(ActionSentenceMappingProvider.class).info(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private static boolean hasMappingForAdapter(final String adapterName){
		return beanMap.containsKey(adapterName);
	}

	public static boolean hasMappingForAction(
		final String adapterName, 
		final String actionId
	) {
		final List<Map<String,String>> list = beanMap.get(adapterName);
		return list == null ? false : list.stream().anyMatch(m -> m.containsKey(actionId));
	}

	public static String getMappingForAction(
			final String adapterName, 
			final String actionId
			) {
		if(ActionSentenceMappingProvider.hasMappingForAdapter(adapterName) && ActionSentenceMappingProvider.hasMappingForAction(adapterName, actionId)) {
			return beanMap.get(adapterName).stream().filter(m -> m.containsKey(actionId)).findFirst().get().get(actionId);
		}
		return null;
	}
}