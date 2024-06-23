package assistant.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author alfredo
 * 
 */
public class DatabaseConnection {
	
	/**
	 * @author alfredo
	 */
	public interface RunnableSQL {
		public void run(Connection connection) throws SQLException;
	}
	
	private Connection connection;
	private DatabaseCredentials configuration;
	
	/**
	 * Creates a Database connection that can be
	 * used throughout the application. Once the application
	 * ends, it gets cleaned up automatically.
	 * 
	 * @param connectionDriver
	 * @param credentials
	 */
	public DatabaseConnection(DatabaseCredentials configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * @return database connection driver type
	 */
	public String getDriver() {
		return configuration.getDriver();
	}
	
	/**
	 * @return true if connection still active
	 * @throws SQLException
	 */
	public boolean isConnected() throws SQLException {
		if(connection == null)
			return false;
		return !connection.isClosed();
	}
	
	/**
	 * Establishes a private connection and 
	 * runs a targeted query for the target database engine.
	 * 
	 * @param queries
	 */
	public void establishConnection(RunnableSQL queries) {
		try {
			// Connect to database
			this.connect();
			
			// Join the connection and 
			// do all cool stuff with database
			queries.run(connection);
			
		} catch (SQLException e) {
			// Print errors
			e.printStackTrace();
		} finally {
			// Disconnect from database always
			this.disconnect();
		}
	}
	
	/**
	 * Disconnects the current connection from database.
	 */
	public void disconnect() {
		try {
			// End the connection if possible
			if(isConnected())
				this.connection.close();
		} catch (SQLException e) {
			// Connection as already disconnected
			System.out.println("Database has already disconnected.");
		}
	}
	
	private void connect() throws SQLException {
		if(configuration == null)
			throw new IllegalArgumentException("You must enter credentials in order to establish a connection!");
		
		// If not connected, imply connection
		if(isConnected())
			return;
		
		try {
			// This must be included so that the 
			// Application once compiled into a jar-file
			// Can execute with no problem
			Class.forName(configuration.getDriver());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// Establish a new connection with driver from credentials
		this.connection = DriverManager.getConnection(
			configuration.getUrl(),      
			configuration.getUsername(), 
			configuration.getPassword()  
		);
	}
}
