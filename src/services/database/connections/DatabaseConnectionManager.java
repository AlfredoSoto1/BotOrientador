package services.database.connections;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Alfredo
 */
public final class DatabaseConnectionManager {
	
	private static DatabaseConnectionManager singleton;
	
	/**
	 * @return DatabaseConnectionFactory singleton instance
	 */
	public static DatabaseConnectionManager instance() {
		if (singleton == null)
			return singleton = new DatabaseConnectionManager();
		return singleton;
	}
	
	/**
	 * Destroys and frees data from the singleton
	 */
	public static void dispose() {
		if(singleton == null)
			throw new IllegalStateException("Cannot dispose null singleton");
		singleton.cleanUp();
	}
	
	private Map<String, DatabaseConnection> connections;
	
	private DatabaseConnectionManager() {
		this.connections = new HashMap<>();
	}
	
	/**
	 * Adds a new Database connection, this way the 
	 * manager can control its proper flow while program is running
	 * 
	 * @param connection
	 */
	public void addDatabaseConnection(DatabaseConnection connection) {
		this.connections.put(connection.getName(), connection);
	}
	
	/**
	 * @param connectionName
	 * @return database connection
	 */
	public Optional<DatabaseConnection> getConnection(String connectionName) {
		return Optional.ofNullable(connections.get(connectionName));
	}
	
	private void cleanUp() {
		// Disconnect from all active connections
		// if any before the program ends
		for(Map.Entry<String, DatabaseConnection> entry : connections.entrySet()) 
			entry.getValue().disconnect();
		
		// Clear all connection references
		connections.clear();
	}
}
