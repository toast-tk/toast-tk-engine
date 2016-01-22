package com.synaptix.toast.adapter.swing.handler.button;

import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.synaptix.toast.adapter.swing.handler.ISwingwidgetActionHandler;
import com.synaptix.toast.adapter.swing.utils.FestRobotInstance;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.report.TestResult.ResultKind;

public class JButtonActionHandler implements
		ISwingwidgetActionHandler<JButton, String, CommandRequest> {
	

	private static final Logger LOG = LogManager.getLogger(JButtonActionHandler.class);

	@Override
	public String handle(final JButton button, CommandRequest command) {
		switch (command.action) {
		case CLICK:
			final CountDownLatch latch = new CountDownLatch(1);
			FestRobotInstance.runOutsideEDT(new Runnable() {
				@Override
				public void run() {
					button.doClick();
					latch.countDown();
				}
			});
			try {
				latch.await();
				return ResultKind.SUCCESS.name();
			} catch (InterruptedException e) {
				LOG.error(e.getMessage(), e);
				return ResultKind.ERROR.name();
			}
		default:
			throw new IllegalArgumentException("Unsupported command for JButton: " + command.action.name());
		}
	}
}
