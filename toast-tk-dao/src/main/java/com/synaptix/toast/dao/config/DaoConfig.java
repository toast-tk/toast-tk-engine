package com.synaptix.toast.dao.config;

public class DaoConfig {

	private final String mongoServer;

	private final int mongoPort;
	
	public DaoConfig(
		final String mongoServer,
		final int mongoPort
	) {
		this.mongoServer = mongoServer;
		this.mongoPort = mongoPort;
	}

	public String getMongoServer() {
		return mongoServer;
	}

	public int getMongoPort() {
		return mongoPort;
	}
}