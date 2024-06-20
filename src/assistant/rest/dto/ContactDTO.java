/**
 * 
 */
package assistant.rest.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Alfredo
 */
public class ContactDTO {
	
	private int id;
	private String email;
	
	private List<ProjectDTO> projects;
	private List<OrganizationDTO> organizations;
	private List<ExtensionDTO> extensions;
	private List<WebpageDTO> webpages;
	private List<SocialMediaDTO> socialmedias;
	
	public ContactDTO() {
		this.projects = new LinkedList<>();
		this.organizations = new LinkedList<>();
		this.extensions = new LinkedList<>();
		this.webpages = new LinkedList<>();
		this.socialmedias = new LinkedList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<ProjectDTO> getProjects() {
		return projects;
	}

	public void addProjects(ProjectDTO project) {
		this.projects.add(project);
	}

	public List<OrganizationDTO> getOrganizations() {
		return organizations;
	}

	public void addOrganizations(OrganizationDTO organization) {
		this.organizations.add(organization);
	}

	public List<ExtensionDTO> getExtensions() {
		return extensions;
	}

	public void addExtensions(ExtensionDTO extension) {
		this.extensions.add(extension);
	}

	public List<WebpageDTO> getWebpages() {
		return webpages;
	}

	public void addWebpages(WebpageDTO webpage) {
		this.webpages.add(webpage);
	}

	public List<SocialMediaDTO> getSocialmedias() {
		return socialmedias;
	}

	public void addSocialmedias(SocialMediaDTO socialmedia) {
		this.socialmedias.add(socialmedia);
	}
}
