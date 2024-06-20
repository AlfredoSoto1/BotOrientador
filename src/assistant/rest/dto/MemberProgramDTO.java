/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class MemberProgramDTO {
	
	private int progid, fdepid;
	private String name;
	
	public MemberProgramDTO() {
		
	}

	public int getProgid() {
		return progid;
	}

	public void setProgid(int progid) {
		this.progid = progid;
	}

	public int getFdepid() {
		return fdepid;
	}

	public void setDepid(int fdepid) {
		this.fdepid = fdepid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
