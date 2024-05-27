package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import application.database.DatabaseConnections.ConnectableDatabase;
import server.crud.connections.Credentials;

/**
 * 
 * @author Alfredo
 */
public class MSAccessConnection {
	
	/*
	 * Private members
	 */
	private Connection connection;
	private Credentials credentials;
	
	/**
	 * This creates a connection to 
	 * a Microsoft access file (database)
	 */
	public MSAccessConnection() {
		this.connection = null;
		this.credentials = null;
	}
	
	/**
	 * Establishes the credentials to be
	 * used when estableshing a connection to DB
	 * 
	 * @param credentials
	 */
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	
	/**
	 * Checks connection to see if its connected
	 * or closed or no connection has been established
	 * 
	 * @return true if connection still active
	 * @throws SQLException
	 */
	public boolean isConnected() throws SQLException {
		if(connection == null)
			return false;
		return !connection.isClosed();
	}
	
	/**
	 * Connects to DataBase with credentials provided
	 * when DataBase connection was created initially
	 * 
	 * @throws SQLException
	 */
	public void connect() throws SQLException {
		if(credentials == null)
			throw new IllegalArgumentException("You must enter credentials in order to establish a connection!");
		// check connection
		if(isConnected())
			return;
		// Establish connection
		this.connection = DriverManager.getConnection(credentials.getUrl(), credentials.getUser(), credentials.getPassword());
	}
	
	/**
	 * Disconnects from MS-DataBase
	 * 
	 * @throws SQLException
	 */
	public void disconnect() throws SQLException {
		if(!isConnected())
			throw new SQLException("Already disconnected from database!");
		this.connection.close();
	}
	
	/**
	 * Checks whether if 'this' connection
	 * is set to read only
	 * 
	 * @return true if connection is set to be read-only
	 * @throws SQLException
	 */
	public boolean isReadOnly() throws SQLException {
		return connection.isReadOnly();
	}
	
	/**
	 * @return basic SQL connection
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Joins a connection for a given database connection
	 * to access database. This prepares the connection using a
	 * lambda function without worrying about trying / catching exceptions
	 * since this does it automatically
	 * 
	 * @param dbConnection
	 * @param connection
	 */
	public void joinConnection(ConnectableDatabase connection) {
		try {
			// Connect to database
			this.connect();
			
			// Join the connection and 
			// do all cool stuff with database
			connection.join();
			
			// Disconnect from database
			this.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
