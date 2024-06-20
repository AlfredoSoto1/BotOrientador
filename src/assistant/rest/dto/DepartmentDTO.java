/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class DepartmentDTO {
	
	private int id;
	
	private String name;
	private String description;
	private String abreviation;

	private BuildingDTO building;
	
	public DepartmentDTO() {
		
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

	public String getAbreviation() {
		return abreviation;
	}

	public void setAbreviation(String abreviation) {
		this.abreviation = abreviation;
	}

	public BuildingDTO getBuilding() {
		return building;
	}

	public void setBuilding(BuildingDTO building) {
		this.building = building;
	}
	
}
