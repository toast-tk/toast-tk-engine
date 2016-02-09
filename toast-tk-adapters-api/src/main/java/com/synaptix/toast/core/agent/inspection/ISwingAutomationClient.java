package com.synaptix.toast.core.agent.inspection;

import com.synaptix.toast.core.net.request.IIdRequest;

public interface ISwingAutomationClient {

	void highlight(
		String selectedValue);

	void scanUi(
		boolean selected);

	void startRecording();

	void stopRecording();

	void setMode(
		int i);

	void processCustomCommand(
		String command);

	void processCustomCommand(
		final IIdRequest commandRequest);

	void killServer();

	boolean saveObjectsToRepository();

	/**
	 * Check connection to SUT
	 * 
	 * @return
	 */
	boolean isConnected();

	/**
	 * Check connection WebApp Host
	 * @return
	 */
	boolean isConnectedToWebApp();

	/**
	 * Start Swing Recording Mode
	 * */
	void switchToSwingRecordingMode();

	/**
	 * Start Web Recording Mode
	 * */
	void switchToWebRecordingMode();

	boolean isWebMode();

	void startRecording(String url);
	
	void connect();
	
	void disconnect();
}
