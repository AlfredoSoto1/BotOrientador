/**
 * 
 */
package assistant.rest.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Alfredo
 */
public class ServiceDTO {

	private int id;
	private int depid;
	private ContactDTO contact;

	private String name;
	private String description;
	private String availability;
	private List<String> offering;
	private Map<String, String[]> additional;

	public ServiceDTO() {
		this.offering = new ArrayList<>();
		this.additional = new HashMap<>();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDepid() {
		return depid;
	}

	public void setDepid(int depid) {
		this.depid = depid;
	}

	public ContactDTO getContact() {
		return contact;
	}

	public void setContact(ContactDTO contact) {
		this.contact = contact;
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

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public List<String> getOffering() {
		return offering;
	}

	public void setOffering(String offering) {
		ObjectMapper objectMapper = new ObjectMapper();
        try {
        	String[] offers = objectMapper.readValue(offering, String[].class);
        	this.offering.addAll(List.of(offers));
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public Map<String, String[]> getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		ObjectMapper objectMapper = new ObjectMapper();
        try {
        	JsonNode node = objectMapper.readTree(additional);
        	Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String key = entry.getKey();
                
                String[] values = new String[entry.getValue().size()];
                for (int i = 0; i < entry.getValue().size(); i++)
                    values[i] = entry.getValue().get(i).asText();
                this.additional.put(key, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
