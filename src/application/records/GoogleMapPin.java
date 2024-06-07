/**
 * 
 */
package application.records;

/**
 * @author Alfredo
 *
 */
@Deprecated
public class GoogleMapPin {

	private String code;
	private String name;
	private String link;
	
	public GoogleMapPin() {

	}
	
	public GoogleMapPin(String code, String name, String link) {
		this.code = code;
		this.name = name;
		this.link = link;
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
