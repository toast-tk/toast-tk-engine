package com.synaptix.toast.automation.driver.swing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.esotericsoftware.kryonet.FrameworkMessage.KeepAlive;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.synaptix.toast.core.agent.inspection.CommonIOUtils;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.IIdRequest;
import com.synaptix.toast.core.net.request.InitInspectionRequest;
import com.synaptix.toast.core.net.response.ErrorResponse;
import com.synaptix.toast.core.net.response.ExistsResponse;
import com.synaptix.toast.core.net.response.InitResponse;
import com.synaptix.toast.core.net.response.ValueResponse;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.report.TestResult.ResultKind;
import com.synaptix.toast.core.runtime.ErrorResultReceivedException;
import com.synaptix.toast.core.runtime.ITCPClient;
import com.synaptix.toast.core.runtime.ITCPResponseReceivedHandler;

public class RemoteSwingAgentDriverImpl implements IRemoteSwingAgentDriver {

	private static final Logger LOG = LogManager.getLogger(RemoteSwingAgentDriverImpl.class);

	protected final ITCPClient client;

	private static final int RECONNECTION_RATE = 10000;

	private static final int WAIT_TIMEOUT = 30000;

	protected Map<String, Object> existsResponseMap;

	private Map<String, Object> valueResponseMap;

	private final Object VOID_RESULT = new Object();

	protected final String host;

	private boolean started;

	@Inject
	public RemoteSwingAgentDriverImpl(
		@Named("host") String host) {
		this.client = new KryoTCPClient();
		this.started = false;
		this.existsResponseMap = new HashMap<String, Object>();
		this.valueResponseMap = new HashMap<String, Object>();
		this.host = host;
		initListeners();
	}

	private void initListeners() {
		client.addResponseHandler(new ITCPResponseReceivedHandler() {
			@Override
			public void onResponseReceived(
				Object object) {
				if(object instanceof ExistsResponse) {
					handleExistsResponse(object);
				}
				else if(object instanceof ValueResponse) {
					handleValueResponse(object);
				}
				else if(object instanceof ErrorResponse) {
					handleErrorResponse(object);
				}
				else if(object instanceof InitResponse) {
					handleInitResponse(object);
				}
				else {
					handleRemainingResponse(object);
				}
			}
		});
	}

	@Override
	public void start(
		String host) {
		try {
			client.connect(300000, host, CommonIOUtils.DRIVER_TCP_PORT);
			this.started = true;
		}
		catch(IOException e) {
			startConnectionLoop();
		}
	}

	protected void startConnectionLoop() {
		while(!client.isConnected()) {
			connect();
			try {
				Thread.sleep(RECONNECTION_RATE);
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handleRemainingResponse(
		Object object) {
		if(object instanceof IIdRequest) {
			handleResponse((IIdRequest) object);
		}
		else if(!(object instanceof KeepAlive)) {
			LOG.warn(String.format("Unhandled response: %s", object));
		}
	}

	private void handleExistsResponse(
		Object object) {
		ExistsResponse response = (ExistsResponse) object;
		existsResponseMap.put(response.id, response.exists);
	}

	private void handleValueResponse(
		Object object) {
		ValueResponse response = (ValueResponse) object;
		valueResponseMap.put(response.getId(), response);
	}

	private void handleInitResponse(
		Object object) {
		if(LOG.isDebugEnabled()) {
			InitResponse response = (InitResponse) object;
			LOG.debug(response);
		}
	}

	private void handleErrorResponse(
		Object object) {
		ErrorResponse response = (ErrorResponse) object;
		TestResult testResult = new TestResult(response.getMessage(),ResultKind.ERROR);
		// TODO: manage screenshots - Request a screenshot
		if(valueResponseMap.keySet().contains(response.getId())) {
			valueResponseMap.put(response.getId(), testResult);
		}
		else if(existsResponseMap.keySet().contains(response.getId())) {
			existsResponseMap.put(response.getId(), testResult);
		}
		else {
			// notify runner
			LOG.error("Error result received {}", response.getMessage());
		}
	}

	public void connect() {
		try {
			client.reconnect();
			this.started = true;
		}
		catch(Exception e) {
			LOG.error(String.format("Server unreachable, reattempting to connect in %d !", RECONNECTION_RATE / 1000));
		}
	}

	@Override
	public void process(
		IIdRequest request) {
		checkConnection();
		init();
		if(request.getId() != null) {
			existsResponseMap.put(request.getId(), VOID_RESULT);
		}
		// TODO: block any request with No ID !!
		client.sendRequest(request);
	}

	private void checkConnection() {
		if(!started) {
			start(host);
		}
		if(!client.isConnected()) {
			connect();
		}
	}

	/**
	 * to call before any request
	 * 
	 * @return
	 */
	public void init() {
		checkConnection();
		InitInspectionRequest request = new InitInspectionRequest();
		client.sendRequest(request);
	}

	@Override
	public boolean waitForExist(
		String reqId)
		throws TimeoutException, ErrorResultReceivedException {
		boolean res = false;
		int countTimeOut = WAIT_TIMEOUT;
		int incOffset = 500;
		client.keepAlive();
		if(existsResponseMap.containsKey(reqId)) {
			while(VOID_RESULT.equals(existsResponseMap.get(reqId))) {
				try {
					client.keepAlive();
					Thread.sleep(500);
					countTimeOut = countTimeOut - incOffset;
					if(countTimeOut <= 0) {
						existsResponseMap.remove(reqId);
						throw new TimeoutException("No Response received for request: " + reqId + " after "
							+ (WAIT_TIMEOUT / 1000) + "s !");
					}
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(existsResponseMap.get(reqId) instanceof TestResult) {
				throw new ErrorResultReceivedException((TestResult) existsResponseMap.get(reqId));
			}
			res = (Boolean) existsResponseMap.get(reqId);
			existsResponseMap.remove(reqId);
		}
		return res;
	}

	@Override
	public TestResult processAndWaitForValue(
		IIdRequest request)
		throws IllegalAccessException, TimeoutException, ErrorResultReceivedException {
		TestResult res = new TestResult();
		final String idRequest = request.getId();
		if(idRequest == null) {
			throw new IllegalAccessException("Request requires an Id to wait for a value.");
		}
		init();
		valueResponseMap.put(idRequest, VOID_RESULT);
		client.sendRequest(request);
		ValueResponse valueResponse = waitForValue(request);
		res.setScreenShot(valueResponse.getBase64ScreenShot());
		res.setMessage(valueResponse.value);
		return res;
	}

	private ValueResponse waitForValue(
		final IIdRequest request)
		throws TimeoutException, ErrorResultReceivedException {
		client.keepAlive();
		final String idRequest = request.getId();
		ValueResponse res = null;
		int countTimeOut = WAIT_TIMEOUT;
		int incOffset = 500;
		if(valueResponseMap.containsKey(idRequest)) {
			while(VOID_RESULT.equals(valueResponseMap.get(idRequest))) {
				try {
					client.keepAlive();
					Thread.sleep(incOffset);
					countTimeOut = countTimeOut - incOffset;
					if(countTimeOut <= 0) {
						valueResponseMap.remove(idRequest);
						throw new TimeoutException("No Response received for request: " + idRequest + " after "
							+ (WAIT_TIMEOUT / 1000) + "s !");
					}
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(valueResponseMap.get(idRequest) instanceof TestResult) {
				throw new ErrorResultReceivedException((TestResult) valueResponseMap.get(idRequest));
			}
			res = (ValueResponse) valueResponseMap.get(idRequest);
			valueResponseMap.remove(idRequest);
		}
		return res;
	}

	protected void handleResponse(
		IIdRequest response) {
		// nothing here, check children classes
	}

	@Override
	public void stop() {
		client.close();
	}
}