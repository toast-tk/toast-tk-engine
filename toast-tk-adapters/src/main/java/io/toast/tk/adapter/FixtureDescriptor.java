package io.toast.tk.adapter;

import java.util.Objects;

public class FixtureDescriptor {

	public final String name;

	public final String fixtureType;

	public final String pattern;

	public final String description;

	public FixtureDescriptor(final String name, final String type, final String pattern, final String description) {
		this.name = name;
		this.fixtureType = type;
		this.pattern = pattern;
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name) + Objects.hash(fixtureType) + Objects.hashCode(pattern);
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
			final FixtureDescriptor fixtureDescriptor = (FixtureDescriptor) obj;
			return Objects.equals(name, fixtureDescriptor.name)
					&& Objects.equals(fixtureType, fixtureDescriptor.fixtureType)
					&& Objects.equals(pattern, fixtureDescriptor.pattern);
		}
		return false;
	}
}