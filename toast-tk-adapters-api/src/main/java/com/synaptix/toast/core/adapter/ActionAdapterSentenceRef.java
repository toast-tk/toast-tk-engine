package com.synaptix.toast.core.adapter;

public class ActionAdapterSentenceRef {


	public static final String WEB_COMPONENT = "{{component:web}}";
	
	public static final String SWING_COMPONENT_REGEX = "{{component:swing}}";

	public static final String VALUE_REGEX = "{{value:string}}";

	public static final String TypeValue = "Saisir " + VALUE_REGEX;

	public static final String TypeValueInInput = "Type " + VALUE_REGEX + " in " + SWING_COMPONENT_REGEX;

	public static final String ClickOnIn = "Click on " + SWING_COMPONENT_REGEX + " in " + SWING_COMPONENT_REGEX;

	public static final String ClickOn = "Click on " + SWING_COMPONENT_REGEX;

	public static final String TypeVarIn = "Type " + VALUE_REGEX + " in " + SWING_COMPONENT_REGEX;

	public static final String Wait = "wait for (\\w+) sec";

	public static final String SelectSubMenu = "Selection menu " + SWING_COMPONENT_REGEX + " dans "
		+ SWING_COMPONENT_REGEX;

	public static final String SelectMenuPath = "Selectionner menu " + VALUE_REGEX;

	public static final String StoreComponentValueInVar = "Récupérer la valeur de " + SWING_COMPONENT_REGEX + " dans "
		+ VALUE_REGEX;

	public static final String GetComponentValue = "Récupérer la valeur de " + SWING_COMPONENT_REGEX;

	public static final String SelectContectualMenu = "Selectionner le menu contextuel " + VALUE_REGEX;

	public static final String SelectValueInList = "Selectionner " + VALUE_REGEX + " dans " + SWING_COMPONENT_REGEX;

	public static final String SelectTableRow = "Dans " + SWING_COMPONENT_REGEX + " selectionner la ligne ayant "
		+ VALUE_REGEX;

	public static final String AddValueInVar = "Ajouter " + VALUE_REGEX + " à " + VALUE_REGEX;

	public static final String SubstractValueFromVar = "Retrancher " + VALUE_REGEX + " à " + VALUE_REGEX;

	public static final String MultiplyVarByValue = "Multiplier " + VALUE_REGEX + " par " + VALUE_REGEX;

	public static final String DiviserVarByValue = "Diviser " + VALUE_REGEX + " par " + VALUE_REGEX;

	public static final String RemplacerVarParValue = "Enregister " + VALUE_REGEX + " dans " + VALUE_REGEX;

	public static enum Params {
		VALUE(VALUE_REGEX, "@Value"), COMPONENT(SWING_COMPONENT_REGEX, "@Page.@Item");

		public final String regex;

		public final String metaInfo;

		Params(
			String regex,
			String metaInfo) {
			this.regex = regex;
			this.metaInfo = metaInfo;
		}
	}

	public static enum Types {
		TYPE_IN_INPUT(TypeValueInInput),
		CLICK_ON(ClickOn),
		SELECT_VALUE_IN_LIST(SelectValueInList),
		SELECT_SUB_MENU(SelectMenuPath),
		SELECT_TABLE_ROW(SelectTableRow),
		SELECT_CONTEXTUAL_MENU(SelectContectualMenu);

		public final String regex;

		Types(
			String regex) {
			this.regex = regex;
		}

		public String metaValue() {
			return regex.replace(Params.VALUE.regex, Params.VALUE.metaInfo)
				.replace(Params.COMPONENT.regex, Params.COMPONENT.metaInfo);
		}
	}

}
