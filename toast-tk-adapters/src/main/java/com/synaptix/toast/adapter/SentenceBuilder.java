package com.synaptix.toast.adapter;

import com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.Types;

public class SentenceBuilder {

	String output;

	public SentenceBuilder() {
		this.output = "";
	}

	public SentenceBuilder ofType(final Types type) {
		this.output = type.metaValue();
		return this;
	}

	public SentenceBuilder withPage(final String page) {
		assert output != null;
		final String value = new StringBuilder(page.length() + 1).append('*').append(page).toString();
		replaceInTemplate("@Page", value);
		return this;
	}

	private void replaceInTemplate(final String marker, final String value) {
		this.output = output.replace(marker, value);
	}
	
	public SentenceBuilder withComponent(final String item) {
		assert output != null;
		final String value = new StringBuilder(item.length() + 1).append(item).append('*').toString();
		replaceInTemplate("@Item", value);
		return this;
	}

	public SentenceBuilder withValue(final String value) {
		assert output != null;
		final String newValue = new StringBuilder(value.length() + 2).append('*').append(value).append('*').toString();
		replaceInTemplate("@Value", newValue);
		return this;
	}

	public String build() {
		final String result = output;
		this.output = null;
		return result;
	}
}