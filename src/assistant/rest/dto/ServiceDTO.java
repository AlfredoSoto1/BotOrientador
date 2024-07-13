/*
 * Copyright 2024 Alfredo Soto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

	private String name;
	private String description;
	private String availability;
	private String departmentAbbreviation;
	private String email;
	private String buildingCode;
	private String buildingName;
	
	private List<String> offering;
	private Map<String, String[]> additional;

	private ContactDTO contact;

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

	public String getDepartmentAbbreviation() {
		return departmentAbbreviation;
	}

	public void setDepartmentAbbreviation(String departmentAbbreviation) {
		this.departmentAbbreviation = departmentAbbreviation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBuildingCode() {
		return buildingCode;
	}

	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
}
