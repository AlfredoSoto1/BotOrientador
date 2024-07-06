/**
 * 
 */
package assistant.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.database.SubTransactionResult;
import assistant.rest.dao.FacultyDAO;
import assistant.rest.dto.EmailDTO;
import assistant.rest.dto.FacultyDTO;

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
		
		return result.getValue("count", 0);
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
		return facultyDAO.getAllFaculty(page * size, size, department);
	}
	
	public Optional<FacultyDTO> getProfessor(EmailDTO email) {
		return facultyDAO.getProfessor(email);
	}
	
	public int addProfessor(FacultyDTO professor) {
		return facultyDAO.insertProfessor(professor);
	}
}
