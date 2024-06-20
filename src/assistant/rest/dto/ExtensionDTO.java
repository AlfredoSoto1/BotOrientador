/**
 * 
 */
package assistant.rest.dto;

/**
 * @author Alfredo
 */
public class ExtensionDTO {
	
	private int id;
	private String ext;

	public ExtensionDTO() {
		// TODO Auto-generated constructor stub
	}

	public ExtensionDTO(int id, String ext) {
		this.id = id;
		this.ext = ext;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
}
