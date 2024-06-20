/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class AdministrativeStaffEntity {
	
	private int id;
	private int contactid;
	private int serviceid;
	
	private String name;
	private String position;
	
	public AdministrativeStaffEntity() {
		
	}

	public AdministrativeStaffEntity(int id, int contactid, int serviceid, String name, String position) {
		this.id = id;
		this.contactid = contactid;
		this.serviceid = serviceid;
		this.name = name;
		this.position = position;
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

	public int getServiceid() {
		return serviceid;
	}

	public void setServiceid(int serviceid) {
		this.serviceid = serviceid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
}
