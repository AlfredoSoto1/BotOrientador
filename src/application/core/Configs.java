/**
 * 
 */
package application.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import services.database.connections.DatabaseCredentials;

/**
 * @author Alfredo
 * 
 */
public final class Configs {

	public static final String DB_DRIVER = "org.postgresql.Driver";
	public static final String DB_CONNECTION = "Assistant-DB";
	private static final String CONFIG_PATH = "assets/config/";
	
	/**
	 * Reads special .tkn file where bot token is contained
	 * 
	 * @return token
	 */
	public static String token() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(CONFIG_PATH + "bot-token.tkn"));
			
			StringBuilder builder = new StringBuilder();
			
			// Just read the first line
			String line = null;
			while((line = reader.readLine()) != null)
				builder.append(line);
			
			reader.close();
			
			return builder.toString();
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Reads Json containing the database credentials
	 * 
	 * @return database credentials
	 */
	public static DatabaseCredentials databaseCredentials() {
		try {
			return new ObjectMapper().readValue(
				new FileReader(CONFIG_PATH + "credentials.json"), DatabaseCredentials.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
}
