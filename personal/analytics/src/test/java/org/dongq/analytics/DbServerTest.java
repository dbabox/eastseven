package org.dongq.analytics;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.dongq.analytics.utils.DbServer;
import org.hsqldb.Server;

public class DbServerTest {

	public static void main(String[] args) {
		String fileName = System.getProperties().getProperty("user.dir") + "/src/main/resources/jdbc.properties";
		Server server = null;
		try {
			Configuration config = new PropertiesConfiguration(fileName);
			//replace db.path
			config.setProperty("db.path", System.getProperties().getProperty("user.dir") + "/src/test/resources/");
			server = new DbServer(config).getServer();
			server.start();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

}
