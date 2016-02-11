package com.synaptix.toast.core.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InspectPage {

	public String name;

	public List<String> items = new ArrayList<>();

	protected InspectPage() {

	}

	public InspectPage(
		final String item,
		final List<Object> items
	) {
		this.name = item;
		items.stream().forEach(o -> items.add(o.toString()));
	}
}