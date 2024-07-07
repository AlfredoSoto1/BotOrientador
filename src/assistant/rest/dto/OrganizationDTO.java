/**
 * 
 */
package assistant.rest.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alfredo
 */
public class OrganizationDTO {
	
	private int id;
	private String name;
	private String description;
	private String email;
	private String website;
	private List<String> platforms;
	private List<String> urlhandle;
	
	public OrganizationDTO() {
		this.platforms = new ArrayList<>();
		this.urlhandle = new ArrayList<>();
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public List<String> getPlatforms() {
		return platforms;
	}

	public void addPlatforms(String platform) {
		this.platforms.add(platform);
	}

	public List<String> getUrlhandle() {
		return urlhandle;
	}

	public void addUrlhandle(String urlhandle) {
		this.urlhandle.add(urlhandle);
	}
	
}
