package com.synaptix.toast.core.agent.interpret;

public interface IEventInterpreter {

	public enum EventType {
		CHECKBOX_CLICK,
		RADIO_CLICK,
		BUTTON_CLICK,
		CLICK,
		TABLE_CLICK,
		MENU_CLICK,
		POPUP_MENU_CLICK,
		JLIST_CLICK,
		COMBOBOX_CLICK,
		WINDOW_DISPLAY,
		KEY_INPUT,
		BRING_ON_TOP_DISPLAY
	}

	public String onCheckBoxClick(final AWTCapturedEvent eventObject);

	public String onButtonClick(final AWTCapturedEvent eventObject);

	public String onClick(final AWTCapturedEvent eventObject);

	public String onTableClick(final AWTCapturedEvent eventObject);

	public String onMenuClick(final AWTCapturedEvent eventObject);

	public String onComboBoxClick(final AWTCapturedEvent eventObject);

	public String onWindowDisplay(final AWTCapturedEvent eventObject);

	public String onKeyInput(final AWTCapturedEvent eventObject);

	public String onBringOnTop(final AWTCapturedEvent eventObject);

	public String onPopupMenuClick(final AWTCapturedEvent eventObject);

	/**
	 * Checks if the interpreter is connected to a remote repository
	 * hosted within the webapp
	 * 
	 * @return
	 */
	public boolean isConnectedToWebApp();
}
