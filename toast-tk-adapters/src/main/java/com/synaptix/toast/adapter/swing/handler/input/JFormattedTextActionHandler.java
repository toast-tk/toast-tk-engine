package com.synaptix.toast.adapter.swing.handler.input;

import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.synaptix.toast.adapter.swing.handler.ISwingwidgetActionHandler;
import com.synaptix.toast.adapter.swing.utils.FestRobotInstance;
import com.synaptix.toast.core.net.request.CommandRequest;


public class JFormattedTextActionHandler implements ISwingwidgetActionHandler<JFormattedTextField, String, CommandRequest>{

	@Override
	public String handle(
		final JFormattedTextField textField,
		final CommandRequest command) {
		switch(command.action) {
			case SET :
				handleSetAction(textField, command);
				break;
			case CLICK :
				handleClickAction(textField);
				break;
			case GET :
				return handleGetAction(textField);
			default :
				throw new IllegalArgumentException("Unsupported command for JTextField: " + command.action.name());
		}
		return null;
	}

	private String handleGetAction(
		final JFormattedTextField textField) {
		return textField.getText();
	}

	private void handleClickAction(
		final JFormattedTextField textField) {
		FestRobotInstance.getRobot().click(textField);
	}

	private void handleSetAction(
		final JFormattedTextField textField,
		final CommandRequest command) {
		if("date".equals(command.itemType)) {
			handleDateType(textField, command);
		}
		else if("date_text".equals(command.itemType)) {
			handleDateTextType(textField, command);
		}
		else {
			textField.setText(command.value);
		}
	}

	private void handleDateTextType(
		final JFormattedTextField textField,
		final CommandRequest command) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				textField.setText(command.value);
				try {
					textField.commitEdit();
				}
				catch(ParseException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void handleDateType(
		final JFormattedTextField textField,
		final CommandRequest command) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				int value = Integer.parseInt(command.value);
				LocalDate date = LocalDate.now().plusDays(value);
				DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yy");
				String formattedDate = formatter.print(date);
				textField.setText(formattedDate);
				try {
					textField.commitEdit();
				}
				catch(ParseException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
