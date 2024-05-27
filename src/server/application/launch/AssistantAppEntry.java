/**
 * 
 */
package server.application.launch;

import server.application.annotations.RegisterApplication;
import server.application.core.Application;
import server.crud.connections.DatabaseConnection;
import server.crud.connections.DatabaseConnectionManager;
import server.crud.connections.DatabaseCredentials;

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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create a new assistant application to start running
		Application.run(new AssistantAppEntry());
	}

	@Override
	public void init() {
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

		
		// Example of database connection (TEMP)
		
//		DatabaseConnectionManager.instance()
//		.getConnection(DB_CONNECTION)
//		.get()
//		.establishConnection(connection -> {
//			
//			Statement statement = connection.createStatement();
//			
//			String SQL = "select * from student;";
//			
//			ResultSet result = statement.executeQuery(SQL);
//			
//			while(result.next()) {
//				int id = result.getInt("id");
//                String name = result.getString("name");
//                int age = result.getInt("age");
//                String email = result.getString("email");
//
//                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age + ", Email: " + email);
//            }
//			
//			result.close();
//			statement.close();
//		});
//		shutdown();
		
		// Initiate the Discord Application
	}

	@Override
	public void shutdown() {
		// Do some cleaning
		DatabaseConnectionManager.dispose();
	}
}
