/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class TeamEntity {

	private int id;
	private int droleid;

	private String name;
	private String orgname;

	public TeamEntity() {
		
	}
	
	public TeamEntity(int id, int droleid, String name, String orgname) {
		this.id = id;
		this.droleid = droleid;
		this.name = name;
		this.orgname = orgname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDroleid() {
		return droleid;
	}

	public void setDroleid(int droleid) {
		this.droleid = droleid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

}
