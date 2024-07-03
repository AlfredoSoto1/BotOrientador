/**
 * 
 */
package assistant.rest.dto;

import java.util.Objects;

import assistant.discord.object.MemberProgram;

/**
 * @author Alfredo
 */
public class StudentDTO {
	
	private String firstname;
	private String lastname;
	private String initial;
	private String sex;
	
	private String email;
	private MemberProgram program;
	
	public StudentDTO() {

	}

	public StudentDTO(String firstname, String lastname, String initial, String sex, String email, MemberProgram program) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.initial = initial;
		this.sex = sex;
		this.email = email;
		this.program = program;
	}
	
    @Override
    public String toString() {
        return "StudentDTO{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", initial='" + initial + '\'' +
                ", sex='" + sex + '\'' +
                ", email='" + email + '\'' +
                ", program='" + program.getLiteral() + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDTO that = (StudentDTO) o;
        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
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

	public MemberProgram getProgram() {
		return program;
	}

	public void setProgram(MemberProgram program) {
		this.program = program;
	}

}
