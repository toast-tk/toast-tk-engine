package com.synaptix.toast.adapter.swing;

import com.synaptix.toast.adapter.swing.component.DefaultSwingAutoElement;
import com.synaptix.toast.adapter.swing.component.SwingButtonElement;
import com.synaptix.toast.adapter.swing.component.SwingCheckBoxElement;
import com.synaptix.toast.adapter.swing.component.SwingDateElement;
import com.synaptix.toast.adapter.swing.component.SwingInputElement;
import com.synaptix.toast.adapter.swing.component.SwingListElement;
import com.synaptix.toast.adapter.swing.component.SwingMenuElement;
import com.synaptix.toast.adapter.swing.component.SwingMenuItemElement;
import com.synaptix.toast.adapter.swing.component.SwingSynTimeLineElement;
import com.synaptix.toast.adapter.swing.component.SwingTableElement;
import com.synaptix.toast.core.adapter.AutoSwingType;
import com.synaptix.toast.core.runtime.ISwingElement;

public class SwingComponentFactory {

	public static SwingAutoElement getElement(
		ISwingElement element) {
		switch(element.getType()) {
			case button :
				return new SwingButtonElement(element);
			case input :
				return new SwingInputElement(element);
			case menu :
				return new SwingMenuElement(element);
			case menuitem :
				return new SwingMenuItemElement(element);
			case table :
				return new SwingTableElement(element);
			case date :
				return new SwingDateElement(element);
			case timeline :
				return new SwingSynTimeLineElement(element);
			case list :
				return new SwingListElement(element);
			case checkbox :
				return new SwingCheckBoxElement(element);
			default :
				return new DefaultSwingAutoElement(element);
		}
	}

	public static Class<? extends SwingAutoElement> getTypeClass(
		AutoSwingType element) {
		switch(element) {
			case button :
				return SwingButtonElement.class;
			case input :
				return SwingInputElement.class;
			case timeline :
				return SwingSynTimeLineElement.class;
			case menu :
				return SwingMenuElement.class;
			case menuitem :
				return SwingMenuItemElement.class;
			case table :
				return SwingTableElement.class;
			case list :
				return SwingListElement.class;
			case date :
				return SwingDateElement.class;
			case checkbox :
				return SwingCheckBoxElement.class;
			default :
				return DefaultSwingAutoElement.class;
		}
	}

	public static AutoSwingType getClassAutoType(
		Class<?> element) {
		if(element.equals(SwingButtonElement.class)) {
			return AutoSwingType.button;
		}
		else if(element.equals(SwingInputElement.class)) {
			return AutoSwingType.input;
		}
		else if(element.equals(SwingSynTimeLineElement.class)) {
			return AutoSwingType.timeline;
		}
		else if(element.equals(SwingMenuElement.class)) {
			return AutoSwingType.menu;
		}
		else if(element.equals(SwingMenuItemElement.class)) {
			return AutoSwingType.menuitem;
		}
		else if(element.equals(SwingTableElement.class)) {
			return AutoSwingType.table;
		}
		else if(element.equals(SwingListElement.class)) {
			return AutoSwingType.list;
		}
		else if(element.equals(SwingDateElement.class)) {
			return AutoSwingType.date;
		}
		else if(element.equals(SwingCheckBoxElement.class)) {
			return AutoSwingType.checkbox;
		}
		else {
			return AutoSwingType.other;
		}
	}
}
