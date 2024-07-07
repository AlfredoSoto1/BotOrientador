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
import assistant.rest.dao.ProjectsDAO;
import assistant.rest.dto.ProjectDTO;

/**
 * @author Alfredo
 */
@Service
public class ProjectsService {

	private final ProjectsDAO projectDAO;
	
	@Autowired
	public ProjectsService(ProjectsDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
	
	public List<String> getProjectNames(int page, int size) {
        SubTransactionResult result = projectDAO.queryProjectNames(page, size);
        
        List<String> names = new ArrayList<>();
        for (int i = 0; i < result.rowCount(); i++) {
			ProjectDTO project = new ProjectDTO();
			project.setId(result.getValue("projecid", i));
			project.setName(result.getValue("name", i));
			project.setDescription(result.getValue("description",  i));
			project.setEmail(result.getValue("email", i));
			names.add(result.getValue("gpin",  i));
		}
		return names;
	}
	
	public Optional<ProjectDTO> getProject(String name) {
		SubTransactionResult result = projectDAO.queryProject(name);
		
		if (result.isEmpty())
			return Optional.empty();
		
		ProjectDTO project = new ProjectDTO();
		project.setId(result.getValue("projecid", 0));
		project.setName(result.getValue("name",   0));
		project.setEmail(result.getValue("email", 0));
		project.setDescription(result.getValue("description",  0));
		
		return Optional.of(project);
	}
}
