package com.synaptix.toast.adapter.swing.handler.button;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.synaptix.toast.adapter.swing.handler.ISwingwidgetActionHandler;
import com.synaptix.toast.core.net.request.CommandRequest;


public class JButtonActionHandler implements ISwingwidgetActionHandler<JButton, String, CommandRequest>{

	@Override
	public String handle(
		final JButton button,
		CommandRequest command) {
		switch(command.action) {
			case CLICK :
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						button.doClick(1);
					}
				});
				break;
			default :
				throw new IllegalArgumentException("Unsupported command for JButton: " + command.action.name());
		}
		return null;
	}
}
