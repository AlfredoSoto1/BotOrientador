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
package assistant.rest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.database.SubTransactionResult;
import assistant.rest.dao.OrganizationsDAO;
import assistant.rest.dto.OrganizationDTO;

/**
 * @author Alfredo
 */
@Service
public class OrganizationsService {

	private final OrganizationsDAO organizationDAO;
	
	@Autowired
	public OrganizationsService(OrganizationsDAO projectDAO) {
		this.organizationDAO = projectDAO;
	}
	
	/**
	 * @param page
	 * @param size
	 * @return List of organization names
	 */
	public List<String> getOrganizationNames(int page, int size) {
        SubTransactionResult result = organizationDAO.queryOrganizationNames(page, size);
        
        List<String> names = new ArrayList<>();
        for (int i = 0; i < result.rowCount(); i++) {
			names.add(result.getValue("name", i));
		}
		return names;
	}
	
	/**
	 * @param page
	 * @param size
	 * @return List of organizations
	 */
	public List<OrganizationDTO> getAllOrganizations(int page, int size) {
        SubTransactionResult result = organizationDAO.queryAllOrganizations(page, size);
        
        Map<Integer, OrganizationDTO> orgs = new HashMap<>();
        for (int i = 0; i < result.rowCount(); i++) {
        	if (orgs.containsKey(result.getValue("orgid", i))) {
        		OrganizationDTO org = orgs.get(result.getValue("orgid", i));
        		org.addPlatforms(result.getValue("platform", i));
        		org.addUrlhandle(result.getValue("urlhandle", i));
        		continue;
        	}
        	OrganizationDTO org = new OrganizationDTO();
        	org.setId(result.getValue("orgid",    i));
        	org.setName(result.getValue("name",   i));
        	org.setEmail(result.getValue("email", i));
        	org.setDescription(result.getValue("description", i));
			
        	org.setWebsite(result.getValue("url", i));
        	org.addPlatforms(result.getValue("platform",  i));
        	org.addUrlhandle(result.getValue("urlhandle", i));
			
			orgs.put(org.getId(), org);
		}
		return new ArrayList<>(orgs.values());
	}
	
	/**
	 * @param name
	 * @return Single organization by name
	 */
	public Optional<OrganizationDTO> getOrganization(String name) {
		SubTransactionResult result = organizationDAO.queryOrganization(name);
		
		if (result.isEmpty())
			return Optional.empty();
		
		 Map<Integer, OrganizationDTO> orgs = new HashMap<>();
        for (int i = 0; i < result.rowCount(); i++) {
        	if (orgs.containsKey(result.getValue("orgid", i))) {
        		OrganizationDTO org = orgs.get(result.getValue("orgid", i));
        		org.addPlatforms(result.getValue("platform",  i));
        		org.addUrlhandle(result.getValue("urlhandle", i));
        		continue;
        	}
        	OrganizationDTO org = new OrganizationDTO();
        	org.setId(result.getValue("orgid",    i));
        	org.setName(result.getValue("name",   i));
        	org.setEmail(result.getValue("email", i));
        	org.setDescription(result.getValue("description", i));
			
        	org.setWebsite(result.getValue("url", i));
        	org.addPlatforms(result.getValue("platform",  i));
        	org.addUrlhandle(result.getValue("urlhandle", i));
			
			orgs.put(org.getId(), org);
		}
        
        if (orgs.size() != 1)
        	return Optional.empty();
        
        Map.Entry<Integer, OrganizationDTO> entry = orgs.entrySet().iterator().next();
		return Optional.of(entry.getValue());
	}
}
