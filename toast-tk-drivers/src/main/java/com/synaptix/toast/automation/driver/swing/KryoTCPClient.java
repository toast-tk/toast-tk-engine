package com.synaptix.toast.automation.driver.swing;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.synaptix.toast.core.agent.inspection.CommonIOUtils;
import com.synaptix.toast.core.net.request.IIdRequest;
import com.synaptix.toast.core.net.request.PoisonPill;
import com.synaptix.toast.core.runtime.ITCPClient;
import com.synaptix.toast.core.runtime.ITCPResponseReceivedHandler;

public class KryoTCPClient implements ITCPClient {

	private final Client client;

	public KryoTCPClient() {
		client = new Client(8012*8012, 8012*8012);
		CommonIOUtils.initSerialization(client.getKryo());
		client.start();
	}

	@Override
	public void connect(
		int timeout,
		String host,
		int tcpPort)
		throws IOException {
		client.connect(300000, host, tcpPort);
	}

	@Override
	public void addResponseHandler(
		final ITCPResponseReceivedHandler itcpResponseReceivedHandler) {
		client.addListener(new Listener() {

			@Override
			public void received(
				Connection connection,
				Object object) {
				itcpResponseReceivedHandler.onResponseReceived(object);
			}
		});
	}

	@Override
	public void addConnectionHandler(
		final ITCPResponseReceivedHandler itcpResponseReceivedHandler) {
		client.addListener(new Listener() {

			@Override
			public void connected(
				Connection connection) {
				super.connected(connection);
				itcpResponseReceivedHandler.onResponseReceived(null);
			}
		});
	}

	@Override
	public void addDisconnectionHandler(
		final ITCPResponseReceivedHandler itcpResponseReceivedHandler) {
		client.addListener(new Listener() {

			@Override
			public void disconnected(
				Connection connection) {
				super.disconnected(connection);
				itcpResponseReceivedHandler.onResponseReceived(null);
			}
		});
	}

	@Override
	public boolean isConnected() {
		return client.isConnected();
	}

	@Override
	public void reconnect()
		throws IOException {
		client.reconnect();
	}

	@Override
	public void sendRequest(
		IIdRequest request) {
		client.sendTCP(request);
	}

	@Override
	public void close() {
		client.close();
	}

	@Override
	public void keepAlive() {
		client.sendTCP(FrameworkMessage.keepAlive);
	}
}
