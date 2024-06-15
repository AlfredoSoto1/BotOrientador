package java.service.database;

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
	public interface RunnableQueries {
		public void runQueries(Connection connection) throws SQLException;
	}
	
	private String name;
	private String driver;
	private Connection connection;
	private DatabaseCredentials credentials;
	
	/**
	 * Creates a Database connection that can be
	 * used throughout the application. Once the application
	 * ends, it gets cleaned up automatically.
	 * 
	 * @param connectionName
	 * @param connectionDriver
	 * @param credentials
	 */
	public DatabaseConnection(String connectionName, String connectionDriver, DatabaseCredentials credentials) {
		this.name = connectionName;
		this.driver = connectionDriver;
		this.credentials = credentials;
	}
	
	/**
	 * @return database connection name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return database connection driver type
	 */
	public String getDriver() {
		return driver;
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
	public void establishConnection(RunnableQueries queries) {
		try {
			// Connect to database
			this.connect();
			
			// Join the connection and 
			// do all cool stuff with database
			queries.runQueries(connection);
			
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
			System.out.println(name + " has already disconnected from database.");
		}
	}
	
	private void connect() throws SQLException {
		if(credentials == null)
			throw new IllegalArgumentException("You must enter credentials in order to establish a connection!");
		
		// If not connected, imply connection
		if(isConnected())
			return;
		
		try {
			// This must be included so that the 
			// Application once compiled into a jar-file
			// Can execute with no problem
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// Establish a new connection with driver from credentials
		this.connection = DriverManager.getConnection(
			credentials.url(),      
			credentials.username(), 
			credentials.password()  
		);
	}
}
