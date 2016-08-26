package io.toast.tk.adapter;

import java.util.Objects;

import io.toast.tk.core.adapter.ActionAdapterSentenceRef.Types;

public class SentenceBuilder {

	String output;

	public SentenceBuilder() {
		this.output = "";
	}

	public SentenceBuilder ofType(final Types type) {
		this.output = Objects.requireNonNull(type.metaValue(), "SentenceBuilder.ofType null metaValue : " + type);
		return this;
	}

	public SentenceBuilder withPage(final String page) {
		final String value = new StringBuilder(page.length() + 1).append('*').append(page).toString();
		replaceInTemplate("@Page", value);
		return this;
	}

	private void replaceInTemplate(final String marker, final String value) {
		this.output = output.replace(marker, value);
	}
	
	public SentenceBuilder withComponent(final String item) {
		final String value = new StringBuilder(item.length() + 1).append(item).append('*').toString();
		replaceInTemplate("@Item", value);
		return this;
	}

	public SentenceBuilder withValue(final String value) {
		if(value==null){
			replaceInTemplate("@Value", "**");
		} else {
			final String newValue = new StringBuilder(value.length() + 2).append('*').append(value).append('*').toString();
			replaceInTemplate("@Value", newValue);
		}
		return this;
	}

	public String build() {
		final String result = output;
		this.output = null;
		return result;
	}
}