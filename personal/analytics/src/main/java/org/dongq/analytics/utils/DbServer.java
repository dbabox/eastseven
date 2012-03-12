package org.dongq.analytics.utils;

import org.apache.commons.configuration.Configuration;
import org.hsqldb.Server;

public final class DbServer {

	private Server server = null;

	private Configuration config;

	private String url;
	private String path;
	private String username;
	private String password;
	private String classDriver;
	private int port;

	public DbServer(Configuration dbConfig) {
		this.config = dbConfig;
		
		this.url = config.getString("db.url");
		this.port = config.getInt("db.port");
		this.path = config.getString("db.path");
		this.username = config.getString("db.username");
		this.password = config.getString("db.password");
		this.classDriver = config.getString("db.classDriver");

		//this.path = path.replace("{webapp.root}", System.getProperties().getProperty("user.dir"));

		String[] dbNames = config.getStringArray("db.name");

		this.server = new Server();
		//this.server.checkRunning(true);
		this.server.setAddress("127.0.0.1");
		this.server.setPort(port);

		int index = 0;
		for (String name : dbNames) {
			this.server.setDatabaseName(index, name);
			this.server.setDatabasePath(index, path + name);
			index++;
		}
	}

	public Server getServer() {
		return this.server;
	}

	@Override
	public String toString() {
		return "DbServer [config=" + config + ", url=" + url + ", path=" + path
				+ ", username=" + username + ", password=" + password
				+ ", classDriver=" + classDriver + ", port=" + port + "]";
	}

}
