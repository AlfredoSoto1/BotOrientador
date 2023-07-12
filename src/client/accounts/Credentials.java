package client.accounts;

public class Credentials {
	
	private String user;
	private String password;
	private String url;
	
	public Credentials() {
		
	}
	
	public Credentials(String user, String password, String url) {
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUrl() {
		return url;
	}
}
