/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class StudentDTO {
	
	private String firstname;
	private String lastname;
	private String initial;
	private String sex;
	
	private String email;
	private String program;
	
	public StudentDTO() {

	}

	public StudentDTO(String firstname, String lastname, String initial, String sex, String email, String program) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.initial = initial;
		this.sex = sex;
		this.email = email;
		this.program = program;
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

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

}
