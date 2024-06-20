/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class ExtensionEntity {
	
	private int id;
	private int contactid;
	
	private String extension;
	
	public ExtensionEntity() {
		
	}

	public ExtensionEntity(int id, int contactid, String extension) {
		this.id = id;
		this.contactid = contactid;
		this.extension = extension;
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

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
}
