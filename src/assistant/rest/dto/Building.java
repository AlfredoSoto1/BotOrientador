/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class Building {
	
	private String code;
	private String name;
	private String gpin;
	
	public Building(String code, String name, String gpin) {
		this.code = code;
		this.name = name;
		this.gpin = gpin;
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
