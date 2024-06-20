/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class DepartmentEntity {
	
	private int id;
	private int buildingid;
	
	private String name;
	private String description;
	private String abreviation;
	
	public DepartmentEntity() {
		
	}

	public DepartmentEntity(int id, int buildingid, String name, String description, String abreviation) {
		this.id = id;
		this.buildingid = buildingid;
		this.name = name;
		this.description = description;
		this.abreviation = abreviation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBuildingid() {
		return buildingid;
	}

	public void setBuildingid(int buildingid) {
		this.buildingid = buildingid;
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
	
}
