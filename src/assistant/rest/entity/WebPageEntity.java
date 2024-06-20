/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class WebPageEntity {
	
	private int id;
	private int contactid;
	
	private String url;
	private String description;
	
	public WebPageEntity() {
		
	}

	public WebPageEntity(int id, int contactid, String url, String description) {
		this.id = id;
		this.contactid = contactid;
		this.url = url;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
