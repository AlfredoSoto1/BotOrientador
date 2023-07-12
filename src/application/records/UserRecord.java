/**
 * 
 */
package application.records;

/**
 * @author Alfredo
 *
 */
public class UserRecord {

	private String email;
	private String actualName;
	private String discordUser;
	
	private int memberID;

	public UserRecord() {
		// Hollow record
	}
	
	public UserRecord(int memberID, String email, String actualName, String discordUser) {
		this.memberID = memberID;
		this.email = email;
		this.actualName = actualName;
		this.discordUser = discordUser;
	}

	public int getMemberID() {
		return memberID;
	}
	
	public String getFirstName() {
		return actualName.split(" ")[0];
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getActualName() {
		return actualName;
	}

	public void setActualName(String actualName) {
		this.actualName = actualName;
	}

	public String getDiscordUser() {
		return discordUser;
	}

	public void setDiscordUser(String discordUser) {
		this.discordUser = discordUser;
	}

}
