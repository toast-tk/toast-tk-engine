package io.toast.tk.core.agent.interpret;

import io.toast.tk.core.annotation.Action;
import io.toast.tk.dao.domain.api.test.ITestResult;


public class DefaultEventInterpreter implements IEventInterpreter {

	@Override
	public String onButtonClick(
		AWTCapturedEvent eventObject) {
		return String.format("Cliquer sur le button *%s*", eventObject.componentName);
	}

	@Override
	public String onClick(
		AWTCapturedEvent eventObject) {
		return String.format("Cliquer sur *%s* de type *%s*", eventObject.componentLocator, eventObject.componentType);
	}

	@Override
	public String onTableClick(
		AWTCapturedEvent eventObject) {
		return String.format("Selectionner dans le tableau la ligne ayant *%s*", eventObject.businessValue);
	}

	@Override
	public String onMenuClick(
		AWTCapturedEvent eventObject) {
		return String.format("Choisir le menu *%s*", eventObject.componentName);
	}

	@Override
	public String onComboBoxClick(
		AWTCapturedEvent eventObject) {
		return String.format("Selectionner *%s* dans *%s*", eventObject.businessValue, eventObject.componentName);
	}

	@Override
	public String onWindowDisplay(
		AWTCapturedEvent eventObject) {
		String type = eventObject.componentType;
		if(eventObject.componentType.contains("Dialog")) {
			type = "dialogue";
		}
		return String.format("Affichage %s *%s*", type, eventObject.componentName);
	}

	@Override
	public String onKeyInput(
		AWTCapturedEvent eventObject) {
		return String.format("Saisir *%s* dans *%s*", eventObject.businessValue, eventObject.componentName);
	}

	@Override
	public String onBringOnTop(
		AWTCapturedEvent eventObject) {
		return String
			.format(
				"Selection Fenetre *%s*",
				eventObject.componentName == null || "null".equals(eventObject.componentName) ? eventObject.componentType : eventObject.componentName);
	}

	@Override
	public String onPopupMenuClick(
		AWTCapturedEvent eventObject) {
		return String.format("Selectionner le menu *%s*", eventObject.componentName);
	}

	@Override
	public String onCheckBoxClick(
		AWTCapturedEvent eventObject) {
		return String.format("Cliquer sur la checkbox '%s'", eventObject.componentName);
	}

	@Override
	public boolean isConnectedToWebApp() {
		return false;
	}


	@Override
	public String onOpenPopupMenu(AWTCapturedEvent eventObject) {
		return String.format("Ouvrir menu contextuel de *%s*", eventObject.componentLocator);
	}
}
