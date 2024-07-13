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
import assistant.rest.dao.ServicesDAO;
import assistant.rest.dto.ContactDTO;
import assistant.rest.dto.ServiceDTO;
import assistant.rest.dto.SocialMediaDTO;
import assistant.rest.dto.WebpageDTO;

/**
 * @author Alfredo
 */
@Service
public class ServicesService {
	
	private final ServicesDAO servicesDAO;
	
	@Autowired
	public ServicesService(ServicesDAO serviceDAO) {
		this.servicesDAO = serviceDAO;
	}
	
	public List<String> getServiceNames() {
		SubTransactionResult result = servicesDAO.getServiceNames();

		List<String> names = new ArrayList<>();
		for (int i = 0;i < result.rowCount();i++)
			names.add(result.getValue("name", i));
		return names;
	}
	
	public List<ServiceDTO> getAllServices(int page, int size) {
		SubTransactionResult result = servicesDAO.getAllServices(page * size, size);
		
		Map<Integer, ServiceDTO> services = new HashMap<>();
		for (int i = 0;i < result.rowCount();i++) {
			if (services.containsKey(result.getValue("servid", i))) {
				ContactDTO contact = services.get(result.getValue("servid", i)).getContact();
				
				if ((boolean)result.getValue("has_webpage", i))
					contact.addWebpages(new WebpageDTO(result.getValue("webid", i), result.getValue("url", i), result.getValue("web_title", i)));
				if ((boolean)result.getValue("has_socialmedia", i))
					contact.addSocialmedias(new SocialMediaDTO(result.getValue("socialid", i), result.getValue("urlhandle", i), result.getValue("platform", i)));
				continue;
			}
			
			ServiceDTO service = new ServiceDTO();
			service.setId(result.getValue("servid",   i));
			service.setName(result.getValue("name",   i));
			service.setEmail(result.getValue("email", i));
			service.setOffering(result.getValue("offering",  i));
			service.setBuildingCode(result.getValue("code",  i));
			service.setBuildingName(result.getValue("bname", i));
			service.setAdditional(result.getValue("additional",     i));
			service.setDescription(result.getValue("description",   i));
			service.setAvailability(result.getValue("availability", i));
			service.setDepartmentAbbreviation(result.getValue("abreviation", i));
			
			ContactDTO contact = new ContactDTO();
			contact.setId(result.getValue("contid",   i));
			contact.setEmail(result.getValue("email", i));
			service.setContact(contact);
			
			if ((boolean)result.getValue("has_webpage", i))
				contact.addWebpages(new WebpageDTO(result.getValue("webid", i), result.getValue("url", i), result.getValue("web_title", i)));
			if ((boolean)result.getValue("has_socialmedia", i))
				contact.addSocialmedias(new SocialMediaDTO(result.getValue("socialid", i), result.getValue("urlhandle", i), result.getValue("platform", i)));
			
			services.put(service.getId(), service);
		}
		return new ArrayList<>(services.values());
	}
	
	public Optional<ServiceDTO> getService(String name) {
		SubTransactionResult result = servicesDAO.getService(name);
		
		Map<Integer, ServiceDTO> services = new HashMap<>();
		for (int i = 0;i < result.rowCount();i++) {
			if (services.containsKey(result.getValue("servid", i))) {
				ContactDTO contact = services.get(result.getValue("servid", i)).getContact();
				
				if ((boolean)result.getValue("has_webpage", i))
					contact.addWebpages(new WebpageDTO(result.getValue("webid", i), result.getValue("url", i), result.getValue("web_title", i)));
				if ((boolean)result.getValue("has_socialmedia", i))
					contact.addSocialmedias(new SocialMediaDTO(result.getValue("socialid", i), result.getValue("urlhandle", i), result.getValue("platform", i)));
				continue;
			}
			
			ServiceDTO service = new ServiceDTO();
			service.setId(result.getValue("servid",   i));
			service.setName(result.getValue("name",   i));
			service.setEmail(result.getValue("email", i));
			service.setOffering(result.getValue("offering",  i));
			service.setBuildingCode(result.getValue("code",  i));
			service.setBuildingName(result.getValue("bname", i));
			service.setAdditional(result.getValue("additional",     i));
			service.setDescription(result.getValue("description",   i));
			service.setAvailability(result.getValue("availability", i));
			service.setDepartmentAbbreviation(result.getValue("abreviation", i));
			
			ContactDTO contact = new ContactDTO();
			contact.setId(result.getValue("contid",   i));
			contact.setEmail(result.getValue("email", i));
			service.setContact(contact);
			
			if ((boolean)result.getValue("has_webpage", i))
				contact.addWebpages(new WebpageDTO(result.getValue("webid", i), result.getValue("url", i), result.getValue("web_title", i)));
			if ((boolean)result.getValue("has_socialmedia", i))
				contact.addSocialmedias(new SocialMediaDTO(result.getValue("socialid", i), result.getValue("urlhandle", i), result.getValue("platform", i)));
			
			services.put(service.getId(), service);
		}
		if (services.size() != 1)
			return Optional.empty();
		
		Map.Entry<Integer, ServiceDTO> entry = services.entrySet().iterator().next();
		return Optional.of(entry.getValue());
	}
}
