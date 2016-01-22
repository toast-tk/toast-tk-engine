package com.synaptix.toast.adapter.swing.handler.input;

import java.util.concurrent.CountDownLatch;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.synaptix.toast.adapter.swing.handler.ISwingwidgetActionHandler;
import com.synaptix.toast.adapter.swing.utils.FestRobotInstance;
import com.synaptix.toast.core.net.request.CommandRequest;


public class JTextFieldActionHandler implements ISwingwidgetActionHandler<JTextField, String, CommandRequest>{

	@Override
	public String handle(
		final JTextField textField,
		final CommandRequest command) {
		switch(command.action) {
			case SET :
				return handleSetText(textField, command);
			case CLICK :
				FestRobotInstance.getRobot().click(textField);
				break;
			case GET :
				return textField.getText();
			default :
				throw new IllegalArgumentException("Unsupported command for JTextField: " + command.action.name());
		}
		return null;
	}

	private String handleSetText(
		final JTextField textField,
		final CommandRequest command) {
		if("date".equals(command.itemType)) {
			final CountDownLatch latch = new CountDownLatch(1);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					int value = Integer.parseInt(command.value);
					LocalDate date = LocalDate.now().plusDays(value);
					DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yy");
					String formattedDate = formatter.print(date);
					textField.setText(formattedDate);
					latch.countDown();
				}
			});
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else if("date_text".equals(command.itemType)) {
			final CountDownLatch latch = new CountDownLatch(1);
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					textField.setText(command.value);
					latch.countDown();
				}
			});
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			textField.setText(command.value);
		}
		return command.value;
	}
}
