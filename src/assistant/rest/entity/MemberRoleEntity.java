/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class MemberRoleEntity {
	
	private int id;
	private int memberid;
	private int droleid;
	
	public MemberRoleEntity() {
		
	}

	public MemberRoleEntity(int id, int memberid, int droleid) {
		this.id = id;
		this.memberid = memberid;
		this.droleid = droleid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMemberid() {
		return memberid;
	}

	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}

	public int getDroleid() {
		return droleid;
	}

	public void setDroleid(int droleid) {
		this.droleid = droleid;
	}
}
