package com.synaptix.toast.core.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InspectPage {

	public String name;

	public List<String> items = new ArrayList<String>();

	protected InspectPage() {
	}

	public InspectPage(
		String item,
		List<Object> items) {
		this.name = item;
		for(Object o : items) {
			this.items.add(o.toString());
		}
	}
}
