package com.synaptix.toast.core.driver;

import com.synaptix.toast.core.net.request.IIdRequest;

public interface IClientDriver {

	/**
	 * send the request to remote agent asynchronously
	 * 
	 * @param request
	 */
	void process(final IIdRequest request);

	/**
	 * synchronous request, expecting the agent to reply with a value to use as an
	 * input to the test report
	 * 
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	String processAndWaitForValue(final IIdRequest requestId) throws Exception;

	/**
	 * init the connection, the start command must be called first when
	 * targeting a specific host 
	 * 
	 */
	void init();

	/**
	 * set the agent host the driver will connect to 
	 * @param host
	 */
	void start(final String host);

	/**
	 * wait that the agents replies to an Exist Request
	 * it ensures that the widget has been found by the remote agent
	 * the widget is ready for action
	 * 
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	boolean waitForExist(final String requestId) throws Exception;

	/**
	 * stop the connection
	 */
	void stop();
}