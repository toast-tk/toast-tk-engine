package com.synaptix.toast.adapter.swing.utils;

import java.util.UUID;

import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.CommandRequest;

public class SwingAutoUtils {

	public static boolean confirmExist(
		IRemoteSwingAgentDriver frontEndDriver,
		String locator,
		String type)
		throws Exception {
		int retry = 20;
		while(retry > 0) {
			try {
				if(exists(frontEndDriver, locator, type)) {
					return true;
				}
				Thread.sleep(5000);
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			retry--;
		}
		return false;
	}

	public static boolean exists(
		IRemoteSwingAgentDriver frontEndDriver,
		String locator,
		String type)
		throws Exception{
		final String requestId = UUID.randomUUID().toString();
		frontEndDriver.process(new CommandRequest.CommandRequestBuilder(requestId).with(locator).ofType(type).exists()
			.build());
		return frontEndDriver.waitForExist(requestId);
	}
}
