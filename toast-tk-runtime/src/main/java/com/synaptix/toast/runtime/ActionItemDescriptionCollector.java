package com.synaptix.toast.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.synaptix.toast.runtime.bean.ActionItem;


public class ActionItemDescriptionCollector {

	private static final Logger LOG = LogManager.getLogger(ActionItemDescriptionCollector.class);

	@SuppressWarnings("unchecked")
	public static List<ActionItem> initActionItems() {
		final List<ActionItem> actionItems = new ArrayList<>();
		try(final InputStream actionItemsSource = ActionItemDescriptionCollector.class.getClassLoader().getResourceAsStream("type_descriptor.json");) {
			final String json = IOUtils.toString(actionItemsSource, "UTF-8");
			final Gson g = new Gson();
			final Type typeOfT = new TypeToken<List<ActionItem>>() {/*NOOP*/}.getType();
			actionItems.addAll((List<ActionItem>) g.fromJson(json, typeOfT));
		}
		catch(final IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return actionItems;
	}
}