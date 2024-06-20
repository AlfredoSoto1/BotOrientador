/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class ProgramEntity {

	private int id;
	private int departmentid;

	private String name;
	private String curriculum;

	public ProgramEntity() {

	}

	public ProgramEntity(int id, int departmentid, String name, String curriculum) {
		this.id = id;
		this.departmentid = departmentid;
		this.name = name;
		this.curriculum = curriculum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(int departmentid) {
		this.departmentid = departmentid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(String curriculum) {
		this.curriculum = curriculum;
	}

}
