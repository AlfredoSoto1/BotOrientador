/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class DiscordRoleDTO {
	
	private int id;
	private int serverid;
	private String name; // This is the name that appears on the discord server
	private String effectivename; // This is the global name that we want to refer to this role in this application
	
	public DiscordRoleDTO() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServerid() {
		return serverid;
	}

	public void setServerid(int serverid) {
		this.serverid = serverid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEffectivename() {
		return effectivename;
	}

	public void setEffectivename(String effectivename) {
		this.effectivename = effectivename;
	}
	
}
