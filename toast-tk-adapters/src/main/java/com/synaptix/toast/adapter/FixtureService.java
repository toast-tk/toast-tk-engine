package com.synaptix.toast.adapter;

import com.synaptix.toast.core.adapter.ActionAdapterKind;

public class FixtureService {

	public final Class<?> clazz;

	public final ActionAdapterKind fixtureKind;

	public final String fixtureName;

	public FixtureService(
		Class<?> clazz,
		ActionAdapterKind fixtureKind,
		String fixtureName) {
		this.clazz = clazz;
		this.fixtureKind = fixtureKind;
		this.fixtureName = fixtureName;
	}
}
