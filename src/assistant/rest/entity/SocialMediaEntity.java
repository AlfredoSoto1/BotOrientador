/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class SocialMediaEntity {
	
	private int id;
	private int contactid;
	
	private String platform;
	private String url;
	
	public SocialMediaEntity() {
		
	}

	public SocialMediaEntity(int id, int contactid, String platform, String url) {
		this.id = id;
		this.contactid = contactid;
		this.platform = platform;
		this.url = url;
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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
