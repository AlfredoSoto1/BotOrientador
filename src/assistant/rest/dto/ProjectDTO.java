/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class ProjectDTO {
	
	private int id;
	private String name;
	private String description;
	private String email;
	
	public ProjectDTO() {
		
	}

	public ProjectDTO(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
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
	
}
