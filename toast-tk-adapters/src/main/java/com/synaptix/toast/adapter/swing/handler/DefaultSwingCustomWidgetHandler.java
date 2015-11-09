package com.synaptix.toast.adapter.swing.handler;

import java.awt.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.synaptix.toast.core.guice.ICustomRequestHandler;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.net.request.IIdRequest;
import com.synaptix.toast.core.net.request.TableCommandRequest;

public class DefaultSwingCustomWidgetHandler implements ICustomRequestHandler {

	private static final Logger LOG = LogManager.getLogger(DefaultSwingCustomWidgetHandler.class);

	@Override
	public String hanldeFixtureCall(
		Component component,
		IIdRequest request) throws IllegalAccessException {
		CommandRequest command = (CommandRequest) request;
		ISwingwidgetActionHandler handler = SwingWidgetActionHandlerFactory.getInstance().getHandler(component);
		if(handler != null){
			return (String) handler.handle(component, command);
		}
		throw new IllegalAccessException("No Handler for swing component: "
				+ ToStringBuilder.reflectionToString(component, ToStringStyle.SHORT_PREFIX_STYLE));
	}

	@Override
	public Component locateComponentTarget(
		String item,
		String itemType,
		Component value) {
		return value;
	}

	@Override
	public String processCustomCall(
		CommandRequest command) {
		return null;
	}

	@Override
	public String getName() {
		return "Toast-DefaultSwingWidgetHandler";
	}

	@Override
	public boolean isInterestedIn(
		Component component) {
		return SwingWidgetActionHandlerFactory.getInstance().hasHandlerFor(component.getClass());
	}

	static List<String> list = Collections.unmodifiableList(Arrays.asList(
		CommandRequest.class.getName(),
		TableCommandRequest.class.getName()));

	@Override
	public List<String> getCommandRequestWhiteList() {
		return list;
	}
}