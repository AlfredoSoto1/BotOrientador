/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class PrepaOrientadorDTO {
	
	private String firstname;
	private String lastname;
	private String teamname;
	private String organization;
	
	public PrepaOrientadorDTO() {

	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}
}
