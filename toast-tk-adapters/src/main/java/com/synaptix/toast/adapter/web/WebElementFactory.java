package com.synaptix.toast.adapter.web;

import com.synaptix.toast.adapter.web.component.WebAutoElement;
import com.synaptix.toast.adapter.web.component.WebButtonElement;
import com.synaptix.toast.adapter.web.component.WebInputElement;
import com.synaptix.toast.adapter.web.component.WebLinkElement;
import com.synaptix.toast.adapter.web.component.WebSelectElement;
import com.synaptix.toast.adapter.web.component.WebTableElement;
import com.synaptix.toast.core.adapter.AutoWebType;
import com.synaptix.toast.core.runtime.IWebElement;

public class WebElementFactory {

	public static WebAutoElement getElement(
		IWebElement e) {
		switch(e.getType()) {
			case button :
				return new WebButtonElement(e);
			case input :
				return new WebInputElement(e);
			case select :
				return new WebSelectElement(e);
			case link :
				return new WebLinkElement(e);
			case table:
				return new WebTableElement(e);
			default :
				return null;
		}
	}

	public static Class<? extends WebAutoElement> getTypeClass(
		AutoWebType e) {
		switch(e) {
			case button : return WebButtonElement.class;
			case input : return WebInputElement.class;
			case list : return WebSelectElement.class;
			case select : return WebSelectElement.class;
			case link :	return WebLinkElement.class;
			case table : return WebTableElement.class;
			default :
				return null;
		}
	}

	public static AutoWebType getClassAutoType(
		Class<?> e) {
		if(e.equals(WebButtonElement.class)) {
			return AutoWebType.button;
		}
		else if(e.equals(WebInputElement.class)) {
			return AutoWebType.input;
		}
		else if(e.equals(WebSelectElement.class)) {
			return AutoWebType.select;
		}
		else if(e.equals(WebLinkElement.class)) {
			return AutoWebType.link;
		}
		else if(e.equals(WebTableElement.class)) {
			return AutoWebType.table;
		}
		else {
			return null;
		}
	}
}
