/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class MemberTeamDTO {
	
	private int teamid;
	private int droleid;
	
	private String name;
	private String orgname;
	
	public MemberTeamDTO() {
		
	}

	public int getTeamid() {
		return teamid;
	}

	public void setTeamid(int teamid) {
		this.teamid = teamid;
	}

	public int getDroleid() {
		return droleid;
	}

	public void setDroleid(int droleid) {
		this.droleid = droleid;
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
	
}
