/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class MemberEntity {
	
	private int id;
	private int teamid;
	private int verificationid;
	
	private String funfact;

	public MemberEntity(int id, int teamid, int verificationid, String funfact) {
		this.id = id;
		this.teamid = teamid;
		this.verificationid = verificationid;
		this.funfact = funfact;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTeamid() {
		return teamid;
	}

	public void setTeamid(int teamid) {
		this.teamid = teamid;
	}

	public int getVerificationid() {
		return verificationid;
	}

	public void setVerificationid(int verificationid) {
		this.verificationid = verificationid;
	}

	public String getFunfact() {
		return funfact;
	}

	public void setFunfact(String funfact) {
		this.funfact = funfact;
	}
	
}
