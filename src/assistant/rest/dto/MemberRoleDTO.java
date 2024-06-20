/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class MemberRoleDTO {
	
	private int memid, fdroleid;
	
	public MemberRoleDTO() {
		
	}

	public int getMemid() {
		return memid;
	}

	public void setMemid(int memid) {
		this.memid = memid;
	}

	public int getFdroleid() {
		return fdroleid;
	}

	public void setFdroleid(int fdroleid) {
		this.fdroleid = fdroleid;
	}
	
}
