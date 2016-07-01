package com.synaptix.toast.adapter;

import java.util.Objects;

import com.synaptix.toast.core.adapter.ActionAdapterKind;

public class FixtureService {

	public final Class<?> clazz;

	public final ActionAdapterKind fixtureKind;

	public final String fixtureName;

	public FixtureService(final Class<?> clazz, final ActionAdapterKind fixtureKind, final String fixtureName) {
		this.clazz = clazz;
		this.fixtureKind = fixtureKind;
		this.fixtureName = fixtureName;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(clazz) + Objects.hashCode(fixtureKind) + Objects.hashCode(fixtureName);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() == obj.getClass()) {
			final FixtureService fixtureService = (FixtureService) obj;
			return Objects.equals(clazz, fixtureService.clazz)
					&& Objects.equals(fixtureKind, fixtureService.fixtureKind)
					&& Objects.equals(fixtureName, fixtureService.fixtureName);
		}
		return false;
	}
}