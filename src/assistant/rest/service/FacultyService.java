/**
 * 
 */
package assistant.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public List<EmailDTO> getFacultyEmails() {
		return facultyDAO.getFacultyEmails();
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
