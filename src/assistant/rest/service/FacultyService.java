/**
 * 
 */
package assistant.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.rest.dao.FacultyDAO;
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
	
	public List<FacultyDTO> getAll(int page, int size) {
		return facultyDAO.getAllFaculty(page * size, size);
	}
	
	public Optional<FacultyDTO> getProfessor(int id) {
		return facultyDAO.getProfessor(id);
	}
	
	public int addProfessor(FacultyDTO professor, String departmentAbreviation) {
		return facultyDAO.insertProfessor(professor, departmentAbreviation);
	}
	
}
