package com.synaptix.toast.adapter.swing.handler.input;

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
				handleSetText(textField, command);
				break;
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

	private void handleSetText(
		final JTextField textField,
		final CommandRequest command) {
		if("date".equals(command.itemType)) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					int value = Integer.parseInt(command.value);
					LocalDate date = LocalDate.now().plusDays(value);
					DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yy");
					String formattedDate = formatter.print(date);
					textField.setText(formattedDate);
				}
			});
		}
		else if("date_text".equals(command.itemType)) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					textField.setText(command.value);
				}
			});
		}
		else {
			textField.setText(command.value);
		}
	}
}
