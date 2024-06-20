/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class ServerOwnershipEntity {
	
	private int id;
	private int departmentid;
	
	private long serverid;
	private long logChannel;
	private String joinedAt;
	
	public ServerOwnershipEntity() {
		
	}

	public ServerOwnershipEntity(int id, int departmentid, long serverid, long logChannel, String joinedAt) {
		this.id = id;
		this.departmentid = departmentid;
		this.serverid = serverid;
		this.logChannel = logChannel;
		this.joinedAt = joinedAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(int departmentid) {
		this.departmentid = departmentid;
	}

	public long getServerid() {
		return serverid;
	}

	public void setServerid(long serverid) {
		this.serverid = serverid;
	}

	public long getLogChannel() {
		return logChannel;
	}

	public void setLogChannel(long logChannel) {
		this.logChannel = logChannel;
	}

	public String getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(String joinedAt) {
		this.joinedAt = joinedAt;
	}
	
}
