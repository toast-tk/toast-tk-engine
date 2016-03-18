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

	public String onCheckBoxClick(
		AWTCapturedEvent eventObject);

	public String onButtonClick(
		AWTCapturedEvent eventObject);

	public String onClick(
		AWTCapturedEvent eventObject);

	public String onTableClick(
		AWTCapturedEvent eventObject);

	public String onMenuClick(
		AWTCapturedEvent eventObject);

	public String onComboBoxClick(
		AWTCapturedEvent eventObject);

	public String onWindowDisplay(
		AWTCapturedEvent eventObject);

	public String onKeyInput(
		AWTCapturedEvent eventObject);

	public String onBringOnTop(
		AWTCapturedEvent eventObject);

	public String onPopupMenuClick(
		AWTCapturedEvent eventObject);

	/**
	 * Checks if the interpreter is connected to a remote repository
	 * hosted within the webapp
	 * 
	 * @return
	 */
	public boolean isConnectedToWebApp();
}
