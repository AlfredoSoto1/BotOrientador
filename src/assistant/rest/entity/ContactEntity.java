/**
 * 
 */
package assistant.rest.entity;

/**
 * @author Alfredo
 */
public class ContactEntity {
	
	private int id;
	private String email;
	
	public ContactEntity() {
		
	}

	public ContactEntity(int id, String email) {
		this.id = id;
		this.email = email;
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
	
}
