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

	/**
	 * The host of the mongo db server
	 * @return
	 */
	public String getMongoServer() {
		return mongoServer;
	}

	/**
	 * the port of the mongo db server 
	 * @return
	 */
	public int getMongoPort() {
		return mongoPort;
	}
}