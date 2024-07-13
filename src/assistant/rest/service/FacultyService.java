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
import assistant.rest.dao.FacultyDAO;
import assistant.rest.dto.ContactDTO;
import assistant.rest.dto.EmailDTO;
import assistant.rest.dto.ExtensionDTO;
import assistant.rest.dto.FacultyDTO;
import assistant.rest.dto.WebpageDTO;

/**
 * @author Alfredo
 */
@Service
public class FacultyService {
	
	private final FacultyDAO facultyDAO;
	
	@Autowired
	public FacultyService(FacultyDAO facultyDAO) {
		this.facultyDAO = facultyDAO;
	}
	
	public long getRecordCount(String departmentAbbreviation) {
		SubTransactionResult result = facultyDAO.getFacultyCount(departmentAbbreviation);
		if (result.isEmpty())
			return 0;
		return result.getValue("count");
	}
	
	public List<EmailDTO> getFacultyEmails() {
		List<EmailDTO> emails = new ArrayList<>();
		SubTransactionResult result = facultyDAO.getFacultyEmails();
		
		for (int i = 0;i < result.rowCount();i++) {
			EmailDTO email = new EmailDTO();
			email.setId(result.getValue("facid", i));
			email.setEmail(result.getValue("email", i));
			emails.add(email);
		}
		return emails;
	}
	
	public List<FacultyDTO> getFaculty(int page, int size, String department) {
		SubTransactionResult result = facultyDAO.getAllFaculty(page * size, size, department);
		
		Map<Integer, FacultyDTO> faculty = new HashMap<>();
		for (int i = 0;i < result.rowCount();i++) {
			if (faculty.containsKey(result.getValue("facid", i))) {
				ContactDTO contact = faculty.get(result.getValue("facid", i)).getContact();
				
				if ((boolean)result.getValue("has_webpage", i))
					contact.addWebpages(new WebpageDTO(result.getValue("webid", i), result.getValue("url", i), result.getValue("description", i)));
				if ((boolean)result.getValue("has_extension", i))
					contact.addExtensions(new ExtensionDTO(result.getValue("extid", i), result.getValue("ext", i)));
				
				continue;
			}
			
			FacultyDTO professor = new FacultyDTO();
			
			professor.setId(result.getValue("facid", i));
			professor.setDepartment(result.getValue("abreviation", i));
			
			professor.setName(result.getValue("name", i));
			professor.setOffice(result.getValue("office", i));
			professor.setDescription(result.getValue("description", i));
			professor.setJobentitlement(result.getValue("jobentitlement", i));
			
			ContactDTO contact = new ContactDTO();
			contact.setId(result.getValue("contid", i));
			contact.setEmail(result.getValue("email", i));
			
			if ((boolean)result.getValue("has_webpage", i))
				contact.addWebpages(new WebpageDTO(result.getValue("webid", i), result.getValue("url", i), result.getValue("description", i)));
			if ((boolean)result.getValue("has_extension", i))
				contact.addExtensions(new ExtensionDTO(result.getValue("extid", i), result.getValue("ext", i)));
			
			professor.setContact(contact);
			
			faculty.put(professor.getId(), professor);
		}
		return new ArrayList<>(faculty.values());
	}
	
	public Optional<FacultyDTO> getProfessor(EmailDTO email) {
		SubTransactionResult result = facultyDAO.getProfessor(email);
		
		if (result.isEmpty())
			return Optional.empty();
		
		Map<Integer, FacultyDTO> faculty = new HashMap<>();
		for (int i = 0;i < result.rowCount();i++) {
			if (faculty.containsKey(result.getValue("facid", i))) {
				ContactDTO contact = faculty.get(result.getValue("facid", i)).getContact();
				
				if ((boolean)result.getValue("has_webpage", i))
					contact.addWebpages(new WebpageDTO(result.getValue("webid", i), result.getValue("url", i), result.getValue("description", i)));
				if ((boolean)result.getValue("has_extension", i))
					contact.addExtensions(new ExtensionDTO(result.getValue("extid", i), result.getValue("ext", i)));
				
				continue;
			}
			
			FacultyDTO professor = new FacultyDTO();
			
			professor.setId(result.getValue("facid", i));
			professor.setDepartment(result.getValue("abreviation", i));
			
			professor.setName(result.getValue("name", i));
			professor.setOffice(result.getValue("office", i));
			professor.setDescription(result.getValue("description", i));
			professor.setJobentitlement(result.getValue("jobentitlement", i));
			
			ContactDTO contact = new ContactDTO();
			contact.setId(result.getValue("contid", i));
			contact.setEmail(result.getValue("email", i));
			
			if ((boolean)result.getValue("has_webpage", i))
				contact.addWebpages(new WebpageDTO(result.getValue("webid", i), result.getValue("url", i), result.getValue("description", i)));
			if ((boolean)result.getValue("has_extension", i))
				contact.addExtensions(new ExtensionDTO(result.getValue("extid", i), result.getValue("ext", i)));
			
			professor.setContact(contact);
			
			faculty.put(professor.getId(), professor);
		}
		
		if (faculty.size() != 1)
			return Optional.empty();
		
		Map.Entry<Integer, FacultyDTO> entry = faculty.entrySet().iterator().next();
		return Optional.of(entry.getValue());
	}
	
	public int addProfessor(FacultyDTO professor) {
		return facultyDAO.insertProfessor(professor);
	}
}
