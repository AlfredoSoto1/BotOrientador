/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class PrepaEntity {
	
	private int id;
	private int verificationid;
	
	private String firstname;
	private String lastname;
	private char initial;
	private char sex;
	
	public PrepaEntity() {
		
	}

	public PrepaEntity(int id, int verificationid, String firstname, String lastname, char initial, char sex) {
		this.id = id;
		this.verificationid = verificationid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.initial = initial;
		this.sex = sex;
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

	public char getInitial() {
		return initial;
	}

	public void setInitial(char initial) {
		this.initial = initial;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}
}
