package io.toast.tk.core.guice;

import java.awt.Component;
import java.util.List;
import java.util.Set;

import io.toast.tk.core.net.request.IIdRequest;

/**
 * Interface to implement within toast plugins in order to handle custom swing ui components
 * 
 */
public interface ICustomRequestHandler {

	/**
	 * Method called to perform a given request (command) on the target Component
	 * 
	 * @param target Component
	 * @param request to indicate which action we need to perform on the target Component
	 * @return any response
	 * @throws IllegalAccessException 
	 */
	String hanldeFixtureCall(
		final Component target,
		final IIdRequest request
	) throws IllegalAccessException;

	/**
	 * Locate an element, the method should return the component (value) if it happens that it
	 * is a component the fixture handler would like to process afterward through the {@link ICustomRequestHandler#hanldeFixtureCall(Component, IIdRequest)}
	 * @param item
	 * @param itemType
	 * @param value
	 * @return the located component, null if none
	 */
	Component locateComponentTarget(
		final String item,
		final String itemType,
		final Component value
	);

	/**
	 * Handle a custom request not covered by the framework standard commands
	 * At this point, the plugin is expected to know how to handle this Custom Command Request
	 * 
	 * @param command Custom Command Request
	 */
	String processCustomCall(final IIdRequest command);

	/**
	 * Check the handler is interested in handling the command request
	 * 
	 * @return list of supported requests
	 */
	List<String> getCommandRequestWhiteList();

	/**
	 * A unique name
	 * 
	 * @return the command request handler name
	 */
	String getName();

	/**
	 * Check the handler is interested in handling the Swing component
	 * 
	 * @param component
	 * @return true if the custom request handler can handle the component
	 */
	boolean isInterestedIn(final Component component);

	/**
	 * The list of components managed by the request handler
	 * 
	 * @return the list of components handled by this hanlder
	 */
	Set<Class<?>> getComponentsWhiteList();
}