/**
 * 
 */
package assistant.discord.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Alfredo
 */
public class BotConfiguration {
	
	private String token;
	
	public BotConfiguration() {
		Properties properties = loadProperties();
		this.token = properties.getProperty("bot.token");
	}
	
	/**
	 * Returns the Bot token
	 * 
	 * @return token
	 */
	public String getToken() {
		return token;
	}
	
    private Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config/bot.properties")) {
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
