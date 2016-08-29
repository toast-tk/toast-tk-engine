package io.toast.tk.runtime.block;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.yaml.snakeyaml.Yaml;

import io.toast.tk.runtime.parse.FileHelper;

@SuppressWarnings("unchecked")
public class ActionSentenceMappingProvider {

	static final Map<String, List<Map<String, String>>> BEANS;

	static {
		try (final InputStream resourceAsStream = FileHelper.getInputStream("toast.yml")) {
			BEANS = ((Map<String, List<Map<String, String>>>) new Yaml().load(resourceAsStream));
		} catch (final IOException e) {
			LogManager.getLogger(ActionSentenceMappingProvider.class).error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private static boolean hasMappingForAdapter(final String adapterName) {
		return BEANS.containsKey(adapterName);
	}

	public static boolean hasMappingForAction(
			final String adapterName,
			final String actionId) {
		final List<Map<String, String>> list = BEANS.get(adapterName);
		return list != null && list.stream().anyMatch(m -> m.containsKey(actionId));
	}

	public static String getMappingForAction(
			final String adapterName,
			final String actionId) {
		if (hasMappingForAdaptaterAndAction(adapterName, actionId)) {
			return BEANS.get(adapterName).stream().filter(m -> m.containsKey(actionId)).findFirst().get().get(actionId);
		}
		return null;
	}

	private static boolean hasMappingForAdaptaterAndAction(final String adapterName, final String actionId) {
		return ActionSentenceMappingProvider.hasMappingForAdapter(adapterName) && ActionSentenceMappingProvider.hasMappingForAction(adapterName, actionId);
	}
}