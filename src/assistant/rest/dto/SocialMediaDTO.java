/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class SocialMediaDTO {
	
	private int id;
	private String url;
	private String platform;
	
	public SocialMediaDTO() {
		// TODO Auto-generated constructor stub
	}

	public SocialMediaDTO(int id, String url, String platform) {
		this.id = id;
		this.url = url;
		this.platform = platform;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
}
