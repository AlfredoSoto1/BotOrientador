/**
 * 
 */
package assistant.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Alfredo
 */
public class DatabaseConfiguration {
	
    private String databaseUrl;
    private String username;
    private String password;
    private String driverClassName;

    public DatabaseConfiguration() {
    	Properties properties = loadProperties();
    	this.databaseUrl = properties.getProperty("database.url");
    	this.username = properties.getProperty("database.username");
    	this.password = properties.getProperty("database.password");
    	this.driverClassName = properties.getProperty("database.driver-class-name");
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }
    
    private Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config/database.properties")) {
            if (input == null)
            	throw new IOException("Sorry, unable to find database.properties");
            // Load the properties file
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return properties;
    }
}
