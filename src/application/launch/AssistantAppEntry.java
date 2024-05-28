/**
 * 
 */
package application.launch;

import application.core.Application;
import application.core.RegisterApplication;
import ece.assistant.app.ECEAssistant;
import services.database.connections.DatabaseConnection;
import services.database.connections.DatabaseConnectionManager;
import services.database.connections.DatabaseCredentials;

/**
 * @author Alfredo
 * 
 */
@RegisterApplication(name = "Discord Assistants", version = "v2024.2.SO4")
public class AssistantAppEntry extends Application {
	
	// Read from file (Json file)
	public static final String DB_DRIVER = "org.postgresql.Driver";
	public static final String DB_CONNECTION = "Assistant-DB";
	public static final DatabaseCredentials DB_CREDENTIALS = new DatabaseCredentials("username", "password", "jdbc:postgresql://localhost:5432/sodb");
	
	public static final String DEVELOPER_ROLE_NAME = "BotDeveloper"; // Reserved name for JUST bot developers
	
	private ECEAssistant INEL_ICOM_ASSISTANT;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create a new assistant application to start running
		Application.run(new AssistantAppEntry());
	}

	@Override
	public void start() {
		
		// Load configuration file data here
		// provide the data to the application
		
		// Add a new Database connection to the factory
		DatabaseConnectionManager.instance().addDatabaseConnection(
			/**
			 *  Create a new database connection
			 *  
			 *  This is the database that the entire program will be based on.
			 *  Database location is not accessible anywhere inside the project.
			 */
			new DatabaseConnection(DB_CONNECTION, DB_DRIVER, DB_CREDENTIALS)
		);

		// Initiate the Discord Application
		INEL_ICOM_ASSISTANT = new ECEAssistant("put token here");
		
		// Star the ECE bot
		INEL_ICOM_ASSISTANT.start();
	}
	
	// This cannot be called yet
	@Override
	public void shutdown() {
		DatabaseConnectionManager.dispose();
	}
}
