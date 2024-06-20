/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class DiscordRoleEntity {

	private int id;
	private int seoid;

	private long roleid;
	private String name;
	private String effectivename;

	public DiscordRoleEntity() {

	}

	public DiscordRoleEntity(int id, int seoid, long roleid, String name, String effectivename) {
		this.id = id;
		this.seoid = seoid;
		this.roleid = roleid;
		this.name = name;
		this.effectivename = effectivename;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSeoid() {
		return seoid;
	}

	public void setSeoid(int seoid) {
		this.seoid = seoid;
	}

	public long getRoleid() {
		return roleid;
	}

	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEffectivename() {
		return effectivename;
	}

	public void setEffectivename(String effectivename) {
		this.effectivename = effectivename;
	}

}
