package service.database;

/**
 * @author Alfredo
 */
public record DatabaseCredentials(String username, String password, String url) {
	
	/**
	 * Generates a final Database credentials record.
	 * 
	 * @param username
	 * @param password
	 * @param url
	 */
	public DatabaseCredentials {
		// Validate the credentials for the database here
	}
}
