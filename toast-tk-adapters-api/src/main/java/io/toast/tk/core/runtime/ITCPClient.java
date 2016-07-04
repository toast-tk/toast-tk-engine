package io.toast.tk.core.runtime;

import java.io.IOException;

import io.toast.tk.core.net.request.IIdRequest;

public interface ITCPClient {

	void connect(
		final int timeout,
		final String host,
		final int tcpPort
	) throws IOException;

	boolean isConnected();

	void reconnect() throws IOException;

	void sendRequest(final IIdRequest request);

	void close();

	void keepAlive();

	void addResponseHandler(final ITCPResponseReceivedHandler itcpResponseReceivedHandler);

	void addConnectionHandler(final ITCPResponseReceivedHandler itcpResponseReceivedHandler);

	void addDisconnectionHandler(final ITCPResponseReceivedHandler itcpResponseReceivedHandler);
}