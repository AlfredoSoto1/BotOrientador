/**
 * 
 */
package application.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.ObjectMapper;

import assistant.models.AssistantConfigs;
import services.database.connections.DatabaseConnectionManager;
import services.database.connections.DatabaseCredentials;

/**
 * @author Alfredo
 * 
 */
public final class Configs {

	public static final String DB_DRIVER = "org.postgresql.Driver";
	public static final String DB_CONNECTION = "Assistant-DB";
	private static Configs singleton;
	
	private ObjectMapper objectMapper;
	
	public static Configs get() {
		if(singleton == null)
			return singleton = new Configs();
		return singleton;
	}
	
	private Configs() {
		this.objectMapper = new ObjectMapper();
	}
	
	/**
	 * Reads special .tkn file where bot token is contained
	 * 
	 * @return token
	 */
	public String token() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("assets/bot-token/bot-token.tkn"));
			
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
	 * 
	 * @return
	 */
	public AssistantConfigs assistantConfigs() {
		try {
            return objectMapper.readValue(
    			new FileReader("assets/configs/Assistant.json"), AssistantConfigs.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	/**
	 * Reads Json containing the database credentials
	 * 
	 * @return database credentials
	 */
	public DatabaseCredentials databaseCredentials() {
		try {
			return objectMapper.readValue(
				new FileReader("assets/database/credentials.json"), DatabaseCredentials.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	/**
	 * 
	 * @param serverID
	 * @param department
	 */
	public boolean registerDepartmentServer(long serverID, String department) {
		
		AtomicBoolean success = new AtomicBoolean(false);
		
		// Store in database these parameters for later access
		DatabaseConnectionManager.instance()
		.getConnection(Configs.DB_CONNECTION)
		.get()
		.establishConnection(connection -> {
			
			String sql = """
				insert into ServerOwnership (fdepid, discserid) values (?, ?);
			""";
			PreparedStatement stmt = connection.prepareStatement(department);
			
			success.set(stmt.executeUpdate() > 0);
			
			stmt.close();
		});
		
		return success.get();
	}
	
	/**
	 * 
	 * @param serverID
	 * @return
	 */
	public String validateDepartmentServer(long serverID) {
		// Look in database the department from which the
		// server is sending a request
		return "ece"; // temp
	}
}
