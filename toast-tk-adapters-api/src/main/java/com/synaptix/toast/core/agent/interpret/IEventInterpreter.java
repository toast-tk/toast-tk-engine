package com.synaptix.toast.core.agent.interpret;

public interface IEventInterpreter {

	public enum EventType {
		CHECKBOX_CLICK,
		RADIO_CLICK,
		BUTTON_CLICK,
		RIGHT_CLICK,
		CLICK,
		TABLE_CLICK,
		MENU_CLICK,
		POPUP_MENU_CLICK,
		JLIST_CLICK,
		COMBOBOX_CLICK,
		OPEN_POPUP,
		WINDOW_DISPLAY,
		KEY_INPUT,
		BRING_ON_TOP_DISPLAY
	}

	/**
	 * produced sentence for a click on a checkbox element
	 * 
	 * @param eventObject
	 * @return
	 */
	public String onCheckBoxClick(final AWTCapturedEvent eventObject);

	/**
	 * produced sentence for a click on a button element
	 * 
	 * @param eventObject
	 * @return
	 */
	public String onButtonClick(final AWTCapturedEvent eventObject);

	/**
	 * produced sentence for a click on a random element
	 * 
	 * @param eventObject
	 * @return
	 */
	public String onClick(final AWTCapturedEvent eventObject);

	/**
	 * produced sentence for a click on a table element
	 * 
	 * @param eventObject
	 * @return
	 */
	public String onTableClick(final AWTCapturedEvent eventObject);

	/**
	 * produced sentence for a click on a menu element
	 * 
	 * @param eventObject
	 * @return
	 */
	public String onMenuClick(final AWTCapturedEvent eventObject);

	/**
	 * produced sentence for a click on a combobox element
	 * 
	 * @param eventObject
	 * @return
	 */
	public String onComboBoxClick(final AWTCapturedEvent eventObject);

	/**
	 * produced sentence for a window display event
	 * 
	 * @param eventObject
	 * @return
	 */
	public String onWindowDisplay(final AWTCapturedEvent eventObject);


	/**
	 * produced sentence for a keyboard input event
	 * 
	 * @param eventObject
	 * @return
	 */
	public String onKeyInput(final AWTCapturedEvent eventObject);

	/**
	 * produced sentence when the system brings component on top
	 * 
	 * @param eventObject
	 * @return
	 */
	public String onBringOnTop(final AWTCapturedEvent eventObject);


	/**
	 * produced sentence when we click on a popup menu
	 * 
	 * @param eventObject
	 * @return
	 */
	public String onPopupMenuClick(final AWTCapturedEvent eventObject);

	/**
	 * Checks if the interpreter is connected to a remote repository
	 * hosted within the webapp
	 * 
	 * @return
	 */
	public boolean isConnectedToWebApp();

	/**
	 * produced sentence when a popup menu get opened
	 * 
	 * @param value
	 * @return
	 */
	public String onOpenPopupMenu(final AWTCapturedEvent value);
}
