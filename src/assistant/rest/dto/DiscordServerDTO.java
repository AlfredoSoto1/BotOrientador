/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class DiscordServerDTO {
	
	private int id;
	private long serverId;
	private long logChannelId;
	private String joinedAt;
	private String department;
	private String color;
	
	public DiscordServerDTO() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getServerId() {
		return serverId;
	}

	public void setServerId(long serverid) {
		this.serverId = serverid;
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
