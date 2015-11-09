package com.synaptix.toast.dao.config;

public class Config {

	private String mongoServer;

	private int mongoPort;

	public String getMongoServer() {
		return mongoServer;
	}

	public void setMongoServer(
		String mongoServer) {
		this.mongoServer = mongoServer;
	}

	public int getMongoPort() {
		return mongoPort;
	}

	public void setMongoPort(
		int mongoPort) {
		this.mongoPort = mongoPort;
	}
}
