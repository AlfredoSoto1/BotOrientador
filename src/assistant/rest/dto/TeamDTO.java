/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class TeamDTO {
	
	private int id;
	private String name;
	private String orgname;
	private DiscordRoleDTO teamRole;
	
	public TeamDTO() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public DiscordRoleDTO getTeamRole() {
		return teamRole;
	}

	public void setTeamRole(DiscordRoleDTO teamRole) {
		this.teamRole = teamRole;
	}
	
}
