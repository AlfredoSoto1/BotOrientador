/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class OrientadorEntity {
	
	private int id;
	private int verificationid;
	
	private String firstname;
	private String lastname;
	
	public OrientadorEntity() {
		
	}

	public OrientadorEntity(int id, int verificationid, String firstname, String lastname) {
		this.id = id;
		this.verificationid = verificationid;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVerificationid() {
		return verificationid;
	}

	public void setVerificationid(int verificationid) {
		this.verificationid = verificationid;
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
}
