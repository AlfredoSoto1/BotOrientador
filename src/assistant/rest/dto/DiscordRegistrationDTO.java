/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class DiscordRegistrationDTO {
	
	private int id;
	private int depid;
	private long serverid;
	private long logChannelId;
	private String joinedAt;
	
	public DiscordRegistrationDTO() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDepid() {
		return depid;
	}

	public void setDepid(int depid) {
		this.depid = depid;
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
	
}
