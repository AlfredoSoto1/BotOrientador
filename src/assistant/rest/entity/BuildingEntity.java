/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class BuildingEntity {
	
	private int id;
	private String code;
	private String name;
	private String gpin;
	
	public BuildingEntity() {
		
	}
	
	public BuildingEntity(int id, String code, String name, String gpin) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.gpin = gpin;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGpin() {
		return gpin;
	}

	public void setGpin(String gpin) {
		this.gpin = gpin;
	}
}
