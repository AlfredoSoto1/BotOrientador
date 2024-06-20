/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class OrganizationEntity {
	
	private int id;
	private int contactid;
	
	private String name;
	private String description;
	
	public OrganizationEntity() {
		
	}

	public OrganizationEntity(int id, int contactid, String name, String description) {
		this.id = id;
		this.contactid = contactid;
		this.name = name;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
}
