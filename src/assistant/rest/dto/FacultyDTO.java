/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class FacultyDTO {

	private int id;
	private int depid;

	private String name;
	private String jobentitlement;
	private String description;
	private String office;

	private ContactDTO contact;

	public FacultyDTO() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJobentitlement() {
		return jobentitlement;
	}

	public void setJobentitlement(String jobentitlement) {
		this.jobentitlement = jobentitlement;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public ContactDTO getContact() {
		return contact;
	}

	public void setContact(ContactDTO contact) {
		this.contact = contact;
	}

	public int getDepid() {
		return depid;
	}

	public void setDepid(int depid) {
		this.depid = depid;
	}

}
