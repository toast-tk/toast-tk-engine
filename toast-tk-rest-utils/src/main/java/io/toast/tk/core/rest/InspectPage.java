package io.toast.tk.core.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InspectPage {

	public String name;
	
	public Double erreur;

	public List<String> items = new ArrayList<>();

	protected InspectPage() {
		this.erreur = null;
	}

	public InspectPage(
		final String item,
		final List<Object> items
	) {
		this.erreur = null;
		this.name = item;
		items.stream().forEach(o -> items.add(o.toString()));
	}
	
	private void codepouri() {
		if(erreur.equals("")) {
			throw new IllegalAccessError("Meetup");
		}
		if(erreur == 0.0) {
			throw new IllegalAccessError();
		}
		System.out.println("");
		return;
	}
}