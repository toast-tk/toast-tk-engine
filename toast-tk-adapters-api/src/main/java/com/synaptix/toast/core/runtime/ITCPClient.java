package com.synaptix.toast.core.runtime;

import java.io.IOException;

import com.synaptix.toast.core.net.request.IIdRequest;

public interface ITCPClient {

	void connect(
		int timeout,
		String host,
		int tcpPort)
		throws IOException;

	boolean isConnected();

	void reconnect()
		throws IOException;

	void sendRequest(
		IIdRequest request);

	void close();

	void keepAlive();

	void addResponseHandler(
		ITCPResponseReceivedHandler itcpResponseReceivedHandler);

	void addConnectionHandler(
		ITCPResponseReceivedHandler itcpResponseReceivedHandler);

	void addDisconnectionHandler(
		ITCPResponseReceivedHandler itcpResponseReceivedHandler);
}
