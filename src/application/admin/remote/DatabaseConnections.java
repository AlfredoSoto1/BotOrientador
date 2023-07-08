/**
 * 
 */
package application.admin.remote;

import java.sql.SQLException;

import application.admin.accounts.Credentials;

/**
 * @author Alfredo
 *
 */
public final class DatabaseConnections {

	/**
	 * 
	 * @author Alfredo
	 *
	 */
	public interface ConnectableDatabase {
		public void join() throws SQLException;
	}
	
	private static DatabaseConnections instance;
	
	/**
	 * Initiate the database connection singleton instance
	 * 
	 * This will be used for managing database connections for
	 * multiple databases that this software may use
	 */
	public static final void initiate() {
		if(instance != null)
			throw new IllegalAccessError("Database connections has already initiated");
		instance = new DatabaseConnections();
	}
	
	/**
	 * Disposes all database connections established
	 * after initiation of singleton instance
	 */
	public static final void dispose() {
		if(instance == null)
			throw new IllegalAccessError("Database connections has already disposed");
		instance.endAllConnections();
	}
	
	/**
	 * Returns the instance of this singleton
	 * 
	 * @return DatabaseConnections - singleton
	 */
	public static DatabaseConnections instance() {
		return instance;
	}
	
	/*
	 * Private members
	 */
	private final static Credentials TM_DB_Credentials = new Credentials("Admin", "s909s", "jdbc:ucanaccess://database/TeamMadeDB.accdb");
	
	// Declaration of the built-in database object
	// that this software uses.
	private MSAccessConnection teamMadeDBConnection;
	
	/**
	 * Constructs the database connections singleton
	 */
	private DatabaseConnections() {
		// Create Integra's connection
		teamMadeDBConnection = new MSAccessConnection();
		teamMadeDBConnection.setCredentials(TM_DB_Credentials);
	}
	
	/**
	 * @return DB connection to this software
	 */
	public MSAccessConnection getTeamMadeConnection() {
		return teamMadeDBConnection;
	}
	
	/**
	 * This ends connection with the database
	 * after application ends (safety measures)
	 */
	private void endAllConnections() {
		// First disconnect from Integra's service
		try {
			teamMadeDBConnection.disconnect();
		} catch (SQLException e) {
			// handle exception here
			System.out.println("Disconnected all references to Team Made's database");
		}
	}
}
