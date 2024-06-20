/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class VerificationEntity {
	
	private int id;
	private int programid;

	private String email;
	
	private String timestamp;
	private boolean verified;
	private int verificationCount;
	
	public VerificationEntity() {
		
	}

	public VerificationEntity(int id, int programid, String email, String timestamp, boolean verified, int verificationCount) {
		this.id = id;
		this.programid = programid;
		this.email = email;
		this.timestamp = timestamp;
		this.verified = verified;
		this.verificationCount = verificationCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProgramid() {
		return programid;
	}

	public void setProgramid(int programid) {
		this.programid = programid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public int getVerificationCount() {
		return verificationCount;
	}

	public void setVerificationCount(int verificationCount) {
		this.verificationCount = verificationCount;
	}
}
