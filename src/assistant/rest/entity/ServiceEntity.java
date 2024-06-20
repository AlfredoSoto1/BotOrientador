/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class ServiceEntity {
	
	private int id;
	private int departmentid;
	private int contactid;
	
	private String name;
	private String description;
	private String offering;   // Json
	private String office;
	private String availability;
	private String additional; // Json
	
	public ServiceEntity() {
		
	}

	public ServiceEntity(int id, int departmentid, int contactid, String name, String description, String offering,
			String office, String availability, String additional) {
		this.id = id;
		this.departmentid = departmentid;
		this.contactid = contactid;
		this.name = name;
		this.description = description;
		this.offering = offering;
		this.office = office;
		this.availability = availability;
		this.additional = additional;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOffering() {
		return offering;
	}

	public void setOffering(String offering) {
		this.offering = offering;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}
	
}
