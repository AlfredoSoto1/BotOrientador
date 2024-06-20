/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class FacultyEntity {
	
	private int id;
	private int departmentid;
	private int contactid;
	
	private String name;
	private String jobentitlement;
	private String description;
	
	public FacultyEntity() {
		
	}

	public FacultyEntity(int id, int departmentid, int contactid, String name, String jobentitlement,
			String description) {
		this.id = id;
		this.departmentid = departmentid;
		this.contactid = contactid;
		this.name = name;
		this.jobentitlement = jobentitlement;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(int departmentid) {
		this.departmentid = departmentid;
	}

	public int getContactid() {
		return contactid;
	}

	public void setContactid(int contactid) {
		this.contactid = contactid;
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
	
}
