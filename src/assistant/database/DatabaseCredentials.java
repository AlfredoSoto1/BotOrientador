/**
 * 
 */
package assistant.database;

/**
 * @author Alfredo
 */
public class DatabaseCredentials {
	
    private String url;
    private String username;
    private String password;
    private String driver;

    public DatabaseCredentials(String url, String username, String password, String driver) {
    	this.url = url;
    	this.username = username;
    	this.password = password;
    	this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }
}
