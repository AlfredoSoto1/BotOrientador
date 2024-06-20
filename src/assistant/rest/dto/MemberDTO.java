/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class MemberDTO {
	
	private int verid, memid, generalid;
	
	private String firstname;
	private String lastname;
	private char initial;
	private char sex;
	
	private String email;
	private boolean verified;
	
	private String funfact;
	
	public MemberDTO() {

	}

	public int getVerid() {
		return verid;
	}

	public void setVerid(int verid) {
		this.verid = verid;
	}

	public int getMemid() {
		return memid;
	}

	public void setMemid(int memid) {
		this.memid = memid;
	}

	public int getGeneralid() {
		return generalid;
	}

	public void setGeneralid(int generalid) {
		this.generalid = generalid;
	}

	public String getFunfact() {
		return funfact;
	}

	public void setFunfact(String funfact) {
		this.funfact = funfact;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

}
