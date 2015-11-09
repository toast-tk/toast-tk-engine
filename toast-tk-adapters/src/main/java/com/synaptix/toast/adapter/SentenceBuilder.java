package com.synaptix.toast.adapter;

import com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.Types;

public class SentenceBuilder {

	String output = "";

	public SentenceBuilder() {
	}

	public SentenceBuilder ofType(
		Types type) {
		this.output = type.metaValue();
		return this;
	}

	public SentenceBuilder withPage(
		String page) {
		assert output != null;
		output = output.replace("@Page", "*" + page);
		return this;
	}

	public SentenceBuilder withComponent(
		String item) {
		assert output != null;
		output = output.replace("@Item", item + "*");
		return this;
	}

	public SentenceBuilder withValue(
		String value) {
		assert output != null;
		output = output.replace("@Value", "*" + value + "*");
		return this;
	}

	public String build() {
		String result = new String(output);
		output = null;
		return result;
	}
}
