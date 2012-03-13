package org.dongq.analytics;

import java.io.File;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.ddlutils.task.DdlToDatabaseTask;
import org.apache.ddlutils.task.WriteSchemaToDatabaseCommand;

public final class CreateDatabaseTest {

	public static void main(String[] args) {
		
		String fileName = System.getProperties().getProperty("user.dir") + "/src/main/resources/jdbc.properties";
		try {
			Configuration config = new PropertiesConfiguration(fileName);
			Properties properties = new Properties();
			properties.put("driverClassName", config.getString("db.classDriver"));
			properties.put("url", config.getString("db.url"));
			properties.put("username", config.getString("db.username"));
			properties.put("password", config.getString("db.password"));
			BasicDataSource dataSource = (BasicDataSource)BasicDataSourceFactory.createDataSource(properties);
			DdlToDatabaseTask task = new DdlToDatabaseTask();
			task.addConfiguredDatabase(dataSource);
			task.setSchemaFile(new File(System.getProperties().getProperty("user.dir") + "/src/main/resources/project-schema.xml"));
			task.addWriteSchemaToDatabase(new WriteSchemaToDatabaseCommand());
			task.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
