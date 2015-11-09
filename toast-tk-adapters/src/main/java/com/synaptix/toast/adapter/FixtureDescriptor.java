package com.synaptix.toast.adapter;

public class FixtureDescriptor {

	public final String name;

	public final String fixtureType;

	public final String pattern;

	public final String description;

	public FixtureDescriptor(
		String name,
		String type,
		String pattern, 
		String description) {
		this.name = name;
		this.fixtureType = type;
		this.pattern = pattern;
		this.description = description;
	}
}
