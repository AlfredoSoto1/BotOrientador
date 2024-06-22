/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class RegisteredDiscordServerDTO {
	
	private int id;
	private long serverid;
	private long logChannelId;
	private String joinedAt;
	private String department;
	
	public RegisteredDiscordServerDTO() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getServerid() {
		return serverid;
	}

	public void setServerid(long serverid) {
		this.serverid = serverid;
	}

	public long getLogChannelId() {
		return logChannelId;
	}

	public void setLogChannelId(long logChannelId) {
		this.logChannelId = logChannelId;
	}

	public String getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(String joinedAt) {
		this.joinedAt = joinedAt;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
}
